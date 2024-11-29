package com.pucminas.integrations.udine;

import com.pucminas.commons.utils.JsonUtils;
import com.pucminas.commons.utils.ListUtils;
import com.pucminas.commons.utils.MessageUtils;
import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.google.places.PlacesProperties;
import com.pucminas.integrations.google.places.PlacesService;
import com.pucminas.integrations.google.places.dto.*;
import com.pucminas.integrations.google.speech_to_text.SpeechToTextService;
import com.pucminas.integrations.google.tech_to_speech.TextToSpeechService;
import com.pucminas.integrations.openai.OpenAiService;
import com.pucminas.integrations.udine.vo.*;
import com.pucminas.integrations.wikipedia.WikipediaService;
import com.pucminas.integrations.wikipedia.dto.SearchLike;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@CommonsLog
public class QuestionServiceImpl extends ServiceBase implements QuestionService {

    private PlacesService placesService;
    private SpeechToTextService speechToTextService;
    private TextToSpeechService textToSpeechService;
    private WikipediaService wikipediaService;
    private OpenAiService openAiService;
    private PlacesProperties placesProperties;

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

    @Autowired
    public void setPlacesProperties(PlacesProperties placesProperties) {
        this.placesProperties = placesProperties;
    }

    @Override
    protected String serviceNameKey() {
        return "question.service.name";
    }

    @Override
    public QuestionResponse answerQuestion(QuestionRequest questionRequest) {
        final boolean isAudioQuestion = QuestionFormatType.AUDIO.equals(questionRequest.getFormatType());
        final String question = getUserQuestion(questionRequest);

        final QuestionDefinition questionDefinition = openAiService.createQuestionDefinition(question);
        log.info("\nquestionDefinition: " + JsonUtils.toJsonString(questionDefinition) + "\n");
        if(questionDefinition == null) {
            return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                    .responseMsgKey("questions.interpretable.question")
                    .placePhotos(List.of())
                    .build();
        }

        final List<Place> places = placesService.searchByText(questionDefinition, questionRequest.getLocation());
        if(places.isEmpty()) {
            return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                    .responseMsgKey("questions.nearby.location.not.found")
                    .placePhotos(List.of())
                    .build();
        }

        placesService.complementWithDistance(places, questionRequest.getLocation());
        placesService.complementWithCityName(places);
        sortPlaces(questionDefinition, places);

        final List<PlaceDetails> placeDetails = getPlacesDetails(places);
        QuestionLlmAnswer answer;
        if (questionDefinition.getLocationType().isRestaurant()) {
            answer = answerQuestion("openai.generate.response.for.restaurant", question, placeDetails);
        } else if (questionDefinition.getLocationType().isHotel()) {
            answer = answerQuestion("openai.generate.response.for.hotel", question, placeDetails);
        } else if (questionDefinition.getLocationType().isTouristAttraction()) {
            setWikipediaTitle(placeDetails);
            setWikipediaDescription(placeDetails);
            answer = answerQuestion("openai.generate.response.for.tourist.attraction", question, placeDetails);
        } else {
            answer = answerQuestion("openai.generate.response.for.locations", question, placeDetails);
        }

        if(answer == null) {
            return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                    .responseMsgKey("questions.interpretable.question")
                    .placePhotos(List.of())
                    .build();
        }

        final List<PlacePhoto> photos =  new ArrayList<>();
        if(questionDefinition.isShowPhotos() && CollectionUtils.isNotEmpty(answer.getIdLocations())){
            photos.addAll(getPlacesPhotos(answer.getIdLocations(), places));
        }

        return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                .response(answer.getAnswer())
                .placePhotos(photos)
                .build();
    }

    private QuestionLlmAnswer answerQuestion(final String promptKey, final String question, List<PlaceDetails> placeDetails) {
        final String prompt = MessageUtils.get(promptKey, question, JsonUtils.toJsonString(placeDetails),
            placesProperties.getRadius());

        final String response = openAiService.answerQuestion(prompt);

        try {
            log.info("Resposta obtida do LLM: " + response);
            return JsonUtils.toObject(response, QuestionLlmAnswer.class);
        } catch (Exception e) {
            log.error("Erro ao processar resposta obtida do LLM", e);
            return null;
        }
    }

    private void sortPlaces(QuestionDefinition apiDef, List<Place> places) {
        if (apiDef.getClassification().isDistance()) {
            placesService.sortPlacesByDistance(places);
        } else {
            placesService.sortPlacesByRanting(places);
        }
    }

    private List<PlacePhoto> getPlacesPhotos(List<String> idsLocations, List<Place> places) {
        final List<PlacePhoto> photos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(idsLocations)) {
            final Map<String, Place> placesMap = ListUtils.toMap(places, Place::getId);
            for (String id : idsLocations) {
                final Place place = placesMap.get(id);
                if (place != null) {
                    final PlacePhoto placePhoto = new PlacePhoto();
                    placePhoto.setName(place.getDisplayName().getText());
                    place.getPhotos().stream().map(PlacePhoto::getName)
                         .map(placesService::getPlacePhoto).forEach(placePhoto::addPhoto);
                    photos.add(placePhoto);
                }
            }
        }

        return photos;
    }

    private List<PlaceDetails> getPlacesDetails(List<Place> places) {
        final List<PlaceDetails> placeDetails = new ArrayList<>();
        places.parallelStream().forEach(place -> placeDetails.add(createPlaceDetails(place)));
        return placeDetails;
    }

    private PlaceDetails createPlaceDetails(Place place) {
        final List<String> openingHours = ListUtils.valueOrDefault(place.getCurrentOpeningHours(),
            OpeningHours::getWeekdayDescriptions, place.getWeekdayDescriptions());
        final String name = place.getDisplayName().getText();
        return new PlaceDetails(name, place.getCity(), place.getId(), place.getShortFormattedAddress(), place.getRating(),
            openingHours, null, null, place.getTypes(), place.getDistance(), place.getPriceLevel());
    }

    private void setWikipediaTitle(List<PlaceDetails> places) {
        final List<PlaceInWikipedia> placeInWikipediaList = Collections.synchronizedList(new ArrayList<>());
        places.parallelStream().forEach(place -> {
            final String name = place.getName();
            final List<SearchLike> nearestTitles = wikipediaService.getNearestWikipediaTitles(name);
            if (CollectionUtils.isNotEmpty(nearestTitles)) {
                placeInWikipediaList.add(new PlaceInWikipedia(place.getId(), name, place.getCity(), nearestTitles));
            }
        });

        if(CollectionUtils.isNotEmpty(placeInWikipediaList)){
            final String prompt = MessageUtils.get("openai.find.wikipedia.title", JsonUtils.toJsonString(placeInWikipediaList));
            final String response = openAiService.answerQuestion(prompt);
            if(StringUtils.isNotEmpty(response)){
                try {
                    final List<PlaceInWikipedia> placeInWikipediaListResponse = JsonUtils.toList(response, PlaceInWikipedia.class);
                    final Map<String, PlaceDetails> placeInWikipediaMap = ListUtils.toMap(places, PlaceDetails::getId);

                    placeInWikipediaListResponse.forEach(placeInWikipedia -> {
                        final PlaceDetails place = placeInWikipediaMap.get(placeInWikipedia.getId());
                        if(place != null){
                            place.setWikipediaTitle(placeInWikipedia.getTitle());
                        }
                    });
                } catch (Exception e) {
                    log.error("Erro ao processar resposta obtida do LLM", e);
                }
            }
        }
    }

    private void setWikipediaDescription(List<PlaceDetails> places) {
        places.parallelStream().filter(it -> StringUtils.isNotEmpty(it.getWikipediaTitle())).forEach(place -> {
            final String wikipediaText = wikipediaService.getWikipediaText(place.getWikipediaTitle());
            place.setWikipediaDescription(wikipediaText);
        });
    }

    private String getUserQuestion(QuestionRequest questionRequest) {
        switch (questionRequest.getFormatType()) {
            case AUDIO:
                return speechToTextService.recognizeAudioMP3(questionRequest.getQuestion());
            case TEXT:
                return questionRequest.getQuestion();
            default:
                throw new IllegalArgumentException("Formato não suportado");
        }
    }

}
