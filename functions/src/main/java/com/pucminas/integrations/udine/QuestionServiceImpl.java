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

        final QuestionApiUsage apiDef = openAiService.determineWhichGoogleMapsApiToUse(question);
        if(apiDef == null || apiDef.getApi().isNone()) {
            return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                    .responseMsgKey("questions.interpretable.question")
                    .placePhotos(List.of())
                    .build();
        }

        final List<Place> places = getPlacesBasedOnQuestion(apiDef, questionRequest.getLocation());
        if(places.isEmpty()) {
            return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                    .responseMsgKey("questions.nearby.location.not.found")
                    .placePhotos(List.of())
                    .build();
        }

        placesService.complementWithDistance(places, questionRequest.getLocation());
        placesService.complementWithCityName(places);
        sortPlaces(apiDef, places);

        final List<PlaceDetails> placeDetails = new ArrayList<>();
        QuestionLlmAnswer answer;
        if (apiDef.getLocationType().isRestaurant()) {
            placeDetails.addAll(getPlacesDetails(places));
            answer = answerQuestion("openai.generate.response.for.restaurant", question, placeDetails);
        } else if (apiDef.getLocationType().isHotel()) {
            placeDetails.addAll(getPlacesDetails(places));
            answer = answerQuestion("openai.generate.response.for.hotel", question, placeDetails);
        } else if (apiDef.getLocationType().isTouristAttraction()) {
            placeDetails.addAll(getPlacesDetailsWithContext(places));
            answer = answerQuestion("openai.generate.response.for.tourist.attraction", question, placeDetails);
        } else {
            placeDetails.addAll(getPlacesDetailsWithContext(places));
            answer = answerQuestion("openai.generate.response.for.locations", question, placeDetails);
        }

        if(answer == null) {
            return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                    .responseMsgKey("questions.interpretable.question")
                    .placePhotos(List.of())
                    .build();
        }

        final List<PlacePhoto> photos =  new ArrayList<>();
        if(apiDef.isShowPhotos() && CollectionUtils.isNotEmpty(answer.getIdLocations())){
            photos.addAll(getPlacesPhotos(answer.getIdLocations(), places));
        }

        return QuestionResponse.builder(isAudioQuestion, textToSpeechService::synthesizeTextString)
                .response(answer.getAnswer())
                .placePhotos(photos)
                .build();
    }

    private QuestionLlmAnswer answerQuestion(final String promptKey, final String question, List<PlaceDetails> placeDetails) {
        final String prompt = MessageUtils.get(promptKey, question, JsonUtils.toJsonString(placeDetails));
        final String response = openAiService.answerQuestion(prompt);

        try {
            log.info("Resposta obtida do LLM: " + response);
            return JsonUtils.toObject(response, QuestionLlmAnswer.class);
        } catch (Exception e) {
            log.error("Erro ao processar resposta obtida do LLM", e);
            return null;
        }
    }

    private void sortPlaces(QuestionApiUsage apiDef, List<Place> places) {
        if (apiDef.getClassification().isDistance()) {
            placesService.sortPlacesByDistance(places);
        } else {
            placesService.sortPlacesByRanting(places);
        }
    }

    private List<PlacePhoto> getPlacesPhotos(List<String> idsLocations, List<Place> places){
        final List<PlacePhoto> photos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(idsLocations)) {
            places.stream().filter(it -> it.getId().equals(idsLocations.getFirst()))
                .map(Place::getPhotos).flatMap(Collection::stream).map(PlacePhoto::getName)
                .map(placesService::getPlacePhoto)
                .forEach(photos::add);
        }

        return photos;
    }

    private List<Place> getPlacesBasedOnQuestion(QuestionApiUsage apiUsage, Location location) {
        final PlaceRequestRestrictionCircle circle = new PlaceRequestRestrictionCircle();
        circle.setCenter(location);
        circle.setRadius(placesProperties.getRadius());

        final List<Place> places = new ArrayList<>();
        if (apiUsage.getApi().isSearchNearby()) {
            final PlacesSearchNearbyRequest request = new PlacesSearchNearbyRequest();
            request.setLocationRestriction(new PlaceRequestRestriction(circle));
            request.addIncludedType(apiUsage.locationType());
            final PlacesResponse response = placesService.searchNearby(request);
            places.addAll(response.getPlaces());
        } else if (apiUsage.getApi().isSearchText()) {
            final PlacesSearchTextRequest request = new PlacesSearchTextRequest();
            request.setLocationBias(new PlaceRequestRestriction(circle));
            request.setTextQuery(apiUsage.getTextQuery());
            final PlacesResponse response = placesService.searchText(request);
            places.addAll(response.getPlaces());
            places.removeIf(place -> ListUtils.noneMatch(place.getTypes(), apiUsage.locationType()));
        }

        return places;
    }

    private List<PlaceDetails> getPlacesDetailsWithContext(List<Place> places) {
        final List<PlaceDetails> placeDetails = new ArrayList<>();

        places.parallelStream().forEach(place -> {
            final List<String> openingHours = ListUtils.valueOrDefault(place.getCurrentOpeningHours(),
                OpeningHours::getWeekdayDescriptions, place.getWeekdayDescriptions());
            final String name = place.getDisplayName().getText();
            final String wikipediaText = getLocationDescription(place);
            final PlaceDetails details = new PlaceDetails(name, place.getId(), place.getShortFormattedAddress(),
                place.getRating(), openingHours, wikipediaText, place.getTypes(), place.getDistance());
            placeDetails.add(details);
        });

        return placeDetails;
    }

    private List<PlaceDetails> getPlacesDetails(List<Place> places) {
        final List<PlaceDetails> placeDetails = new ArrayList<>();

        places.parallelStream().forEach(place -> {
            final List<String> openingHours = ListUtils.valueOrDefault(place.getCurrentOpeningHours(),
                    OpeningHours::getWeekdayDescriptions, place.getWeekdayDescriptions());
            final String name = place.getDisplayName().getText();
            final PlaceDetails details = new PlaceDetails(name, place.getId(), place.getShortFormattedAddress(),
                    place.getRating(), openingHours, null, place.getTypes(), place.getDistance());
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
