package com.pucminas.integrations.udine;

import com.pucminas.commons.utils.JsonUtils;
import com.pucminas.commons.utils.ListUtils;
import com.pucminas.commons.utils.MessageUtils;
import com.pucminas.commons.utils.StrUtils;
import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.google.places.PlacesService;
import com.pucminas.integrations.google.places.dto.OpeningHours;
import com.pucminas.integrations.google.places.dto.Place;
import com.pucminas.integrations.google.places.dto.PlacePhoto;
import com.pucminas.integrations.google.speech_to_text.SpeechToTextService;
import com.pucminas.integrations.google.tech_to_speech.TextToSpeechService;
import com.pucminas.integrations.openai.OpenAiService;
import com.pucminas.integrations.udine.vo.*;
import com.pucminas.integrations.wikipedia.WikipediaService;
import com.pucminas.integrations.wikipedia.dto.SearchByTitleAndCity;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@CommonsLog
public class QuestionServiceImpl extends ServiceBase implements QuestionService {

    private PlacesService placesService;
    private SpeechToTextService speechToTextService;
    private TextToSpeechService textToSpeechService;
    private WikipediaService wikipediaService;
    private OpenAiService openAiService;


    @Autowired
    public void setPlacesService(PlacesService placesService) {
        this.placesService = placesService;
    }

    @Autowired
    public void setSpeechToTextService(SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    @Autowired
    public void setTextToSpeechService(TextToSpeechService textToSpeechService) {
        this.textToSpeechService = textToSpeechService;
    }

    @Autowired
    public void setWikipediaService(WikipediaService wikipediaService) {
        this.wikipediaService = wikipediaService;
    }

    @Autowired
    public void setOpenAiService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    protected String serviceNameKey() {
        return "question.service.name";
    }

    @Override
    public QuestionResponse answerQuestion(QuestionRequest questionRequest) {
        final QuestionResponse response = new QuestionResponse();
        response.setFormatType(QuestionFormatType.TEXT);
        response.setResponse("questions.not.possible.understand.audio");

        final List<Place> places = placesService.getPlacesDetails(questionRequest.placesId());
        final List<PlacePhoto> placePhotos = new ArrayList<>();
        final String question = getUserQuestion(questionRequest);
        final List<PlaceDetails> placeDetails = getPlacesDetailsWithContext(places);
        final boolean isAudioQuestion = QuestionFormatType.AUDIO.equals(questionRequest.formatType());

        removeLocationsNoRelatedToQuestion(placeDetails, question);

        if(CollectionUtils.isEmpty(placeDetails)) {
            return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                    .response(MessageUtils.get("questions.nearby.location.not.found"))
                    .placePhotos(placePhotos)
                    .success(false)
                    .build();
        }

        final String promptKey = isAudioQuestion ? "questions.locations.prompt.audio" : "questions.locations.prompt.text";
        final String prompt = MessageUtils.get(promptKey, JsonUtils.toJsonString(QuestionLlmAnswer.LlmSample()),
            JsonUtils.toJsonString(placeDetails), question);
        final String answered = openAiService.answerQuestion(prompt);

        try{
            final QuestionLlmAnswer llmAnswer = JsonUtils.toObject(answered, QuestionLlmAnswer.class);
            if (llmAnswer == null) {
                return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                        .response("Ocorreu um erro no servidor. Tente novamente em alguns minutos")
                        .placePhotos(placePhotos)
                        .success(false)
                        .build();
            }

            if (llmAnswer.getIdLocation() != null) {
                places.stream().filter(it -> it.getId().equals(llmAnswer.getIdLocation()))
                    .map(Place::getPhotos).flatMap(Collection::stream).map(PlacePhoto::getName)
                    .map(placesService::getPlacePhoto)
                    .forEach(placePhotos::add);
            }

            if (StringUtils.isNotBlank(llmAnswer.getAnswer())) {
                return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                        .response(llmAnswer.getAnswer())
                        .placePhotos(placePhotos)
                        .build();
            }
        } catch (Exception e) {
            log.error("Erro ao processar resposta obtida do LLM", e);
            return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                    .response("Ocorreu um erro no servidor. Tente novamente em alguns minutos")
                    .placePhotos(placePhotos)
                    .success(false)
                    .build();
        }

        return response;
    }

    private void removeLocationsNoRelatedToQuestion(List<PlaceDetails> placesDetails, String question) {
        final String promptFilterLocations = MessageUtils.get("questions.identify.question.type", question);
        final String placeType = StrUtils.removeDoubleQuotes(openAiService.answerQuestion(promptFilterLocations));

        if (StringUtils.isNotEmpty(placeType) && !"empty".equals(placeType)) {
            placesDetails.removeIf(it -> ListUtils.noneMatch(it.locationTypes(), placeType));
        }
    }

    private List<PlaceDetails> getPlacesDetailsWithContext(List<Place> places) {
        final List<PlaceDetails> placeDetails = new ArrayList<>();

        places.parallelStream().forEach(place -> {
            final List<String> openingHours = ListUtils.valueOrDefault(place.getCurrentOpeningHours(),
                OpeningHours::getWeekdayDescriptions, place.getWeekdayDescriptions());
            final String name = place.getDisplayName().getText();
            final String wikipediaText = getLocationDescription(place);
            final PlaceDetails details = new PlaceDetails(name, place.getId(), place.getShortFormattedAddress(),
                place.getRating(), openingHours, wikipediaText, place.getTypes());
            placeDetails.add(details);
        });

        return placeDetails;
    }

    /**
     * Returns location description from wikipedia or site scraping
     *
     * @return String
     */
    private String getLocationDescription(Place place) {
        final String name = place.getDisplayName().getText();
        final String wikipediaText = wikipediaService.getWikipediaText(name);
        if (StringUtils.isNotBlank(wikipediaText)
                && !StringUtils.startsWith(wikipediaText, MessageUtils.get("wikipedia.search.title.error", name))
                && !StringUtils.startsWith(wikipediaText, MessageUtils.get("wikipedia.title.not.found", name))) {
            return wikipediaText;
        }

        final String nearestTitle = wikipediaService.getNearestWikipediaTitle(new SearchByTitleAndCity(name, place.getCity()));
        if (StringUtils.isBlank(nearestTitle)) {
            return MessageUtils.get("wikipedia.title.not.found", name);
        }

        return wikipediaService.getWikipediaText(nearestTitle);
    }

    private String getUserQuestion(QuestionRequest questionRequest) {
        switch (questionRequest.formatType()) {
            case AUDIO:
                return speechToTextService.recognizeAudioMP3(questionRequest.question());
            case TEXT:
                return questionRequest.question();
            default:
                throw new IllegalArgumentException("Formato não suportado");
        }
    }

}
