package com.pucminas.integrations.udine;

import com.pucminas.commons.utils.JsonUtils;
import com.pucminas.commons.utils.ListUtils;
import com.pucminas.commons.utils.MessageUtils;
import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.google.places.PlacesService;
import com.pucminas.integrations.google.places.dto.OpeningHours;
import com.pucminas.integrations.google.places.dto.PlaceDetailResponse;
import com.pucminas.integrations.google.places.dto.PlaceDetailResult;
import com.pucminas.integrations.google.speech_to_text.SpeechToTextService;
import com.pucminas.integrations.google.tech_to_speech.TextToSpeechService;
import com.pucminas.integrations.openai.OpenAiService;
import com.pucminas.integrations.udine.vo.PlaceDetails;
import com.pucminas.integrations.udine.vo.QuestionFormatType;
import com.pucminas.integrations.udine.vo.QuestionRequest;
import com.pucminas.integrations.udine.vo.QuestionResponse;
import com.pucminas.integrations.wikipedia.WikipediaService;
import com.pucminas.integrations.wikipedia.dto.SearchByTitleAndCity;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@CommonsLog
public class QuestionServiceImpl extends ServiceBase implements QuestionService {

    private PlacesService placesService;
    private SpeechToTextService speechToTextService;
    private TextToSpeechService textToSpeechService;
    private WikipediaService wikipediaService;
    private OpenAiService openAiService;
    private ScrapingService scrapingService;

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
    public void setScrapingService(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }

    @Override
    protected String serviceNameKey() {
        return "question.service.name";
    }

    @Override
    public QuestionResponse answerQuestion(QuestionRequest questionRequest) {
        final List<PlaceDetails> placeDetails = cacheService.getCachedValueOrNew(getClass(),
            "CACHE_KEY_PLACE_DETAILS", questionRequest.placesId(), this::getPlacesDetailsWithContext);

        removeLocationsNotRelatedToQuestion(placeDetails, questionRequest.question());
        if(CollectionUtils.isEmpty(placeDetails)) {
            return new QuestionResponse(MessageUtils.get("questions.nearby.location.not.found"), QuestionFormatType.TEXT);
        }

        final String question = getUserQuestion(questionRequest);
        final String prompt = MessageUtils.get("questions.locations.prompt", JsonUtils.toJsonString(placeDetails), question);
        final String answered = openAiService.answerQuestion(prompt);

        if (StringUtils.isNotBlank(answered)) {
            if (QuestionFormatType.TEXT.equals(questionRequest.formatType())) {
                return new QuestionResponse(answered, QuestionFormatType.TEXT);
            }
            return new QuestionResponse(textToSpeechService.synthesizeText(answered).getAudioContent(), QuestionFormatType.AUDIO);
        }
        return new QuestionResponse("Não foi possível entender o audio enviado. Verifique a qualidade da gravação e tente novamente.", QuestionFormatType.TEXT);
    }

    @SuppressWarnings("unchecked")
    private void removeLocationsNotRelatedToQuestion(List<PlaceDetails> placesDetails, String question) {
        final String jsonArrayLocations = JsonUtils.toJsonString(placesDetails.stream().map(PlaceDetails::name).toList());
        final String promptFilterLocations = MessageUtils.get("questions.identify.question.type", jsonArrayLocations, question);
        final String response = openAiService.answerQuestion(promptFilterLocations);
        try {
            List<String> locationsNames = JsonUtils.toObject(response, ArrayList.class);
            placesDetails.removeIf(it -> !locationsNames.contains(it.name()));
        } catch (Exception e) {
            log.error(MessageUtils.get("questions.error.converts.openai.response.to.list"), e);
        }
    }

    private List<PlaceDetails> getPlacesDetailsWithContext(List<String> placesId) {
        final List<PlaceDetails> placeDetails = new ArrayList<>();

        placesId.parallelStream().map(placesService::getPlaceDetails).map(PlaceDetailResponse::getResult)
                .forEach(placeResult -> {
                    final List<String> weekdayText = ListUtils.valueOrEmpty(placeResult.getOpeningHours(), OpeningHours::getWeekdayText);
                    final String wikipediaText = getLocationDescription(placeResult);
                    final PlaceDetails details = new PlaceDetails(placeResult.getName(), placeResult.getVicinity(),
                            placeResult.getRating(), weekdayText, wikipediaText, placeResult.getTypes());
                    placeDetails.add(details);
                });

        return placeDetails;
    }

    /**
     * Returns location description from wikipedia or site scraping
     *
     * @return String
     */
    private String getLocationDescription(PlaceDetailResult place) {
        final String wikipediaText = wikipediaService.getWikipediaText(place.getName());
        if (StringUtils.isNotBlank(wikipediaText)
                && !StringUtils.startsWith(wikipediaText, MessageUtils.get("wikipedia.search.title.error", place.getName()))
                && !StringUtils.startsWith(wikipediaText, MessageUtils.get("wikipedia.title.not.found", place.getName()))) {
            return wikipediaText;
        }

        // Não usar scraping por enquanto
        //        if(StringUtils.isNotEmpty(place.getWebsite())){
        //            final List<String> text = scrapingService.scrapAllTextTags(place.getWebsite());
        //        }

        final String nearestTitle = wikipediaService.getNearestWikipediaTitle(new SearchByTitleAndCity(place.getName(), place.getCity()));
        if (StringUtils.isBlank(nearestTitle)) {
            return MessageUtils.get("wikipedia.title.not.found", place.getName());
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
