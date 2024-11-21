package com.pucminas.integrations.udine;

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
import com.pucminas.utils.JsonUtils;
import com.pucminas.utils.ListUtils;
import com.pucminas.utils.MessageUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@CommonsLog
public class QuestionServiceImpl implements QuestionService {

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
    public QuestionResponse answerQuestion(QuestionRequest questionRequest) {
        final List<PlaceDetails> placeDetails = new ArrayList<>();

        questionRequest.placesId().stream().map(placesService::getPlaceDetails).map(PlaceDetailResponse::getResult)
                .forEach(placeResult -> {
                    final List<String> weekdayText = ListUtils.valueOrEmpty(placeResult.getOpeningHours(), OpeningHours::getWeekdayText);
                    final String wikipediaText = getLocationDescription(placeResult);
                    final PlaceDetails details = new PlaceDetails(placeResult.getName(), placeResult.getVicinity(),
                        placeResult.getRating(), weekdayText, wikipediaText, placeResult.getTypes());
                    placeDetails.add(details);
                });

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

    /**
     * Returns location description from wikipedia or site scraping
     * @return String
     */
    private String getLocationDescription(PlaceDetailResult place){
        final String wikipediaText = wikipediaService.getWikipediaText(place.getName());
        if(StringUtils.isNotBlank(wikipediaText)
            && !StringUtils.startsWith(wikipediaText, MessageUtils.get("wikipedia.search.title.error", place.getName()))
            && !StringUtils.startsWith(wikipediaText, MessageUtils.get("wikipedia.title.not.found", place.getName())))
        {
            return wikipediaText;
        }

        final String nearestTitle = wikipediaService.getNearestWikipediaTitle(Arrays.asList(place.getName(), place.getCity()));
        if(StringUtils.isBlank(nearestTitle)){
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
