package com.pucminas.integrations.udine;

import com.pucminas.integrations.google.places.PlacesService;
import com.pucminas.integrations.google.places.dto.OpeningHours;
import com.pucminas.integrations.google.places.dto.PlaceDetailResponse;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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

        questionRequest.placesId().parallelStream().map(placesService::getPlaceDetails).map(PlaceDetailResponse::result)
                .forEach(placeResult -> {
                    final List<String> weekdayText = ListUtils.valueOrEmpty(placeResult.opening_hours(), OpeningHours::weekday_text);
                    final String wikipediaText = wikipediaService.getWikipediaText(placeResult.name());
                    final PlaceDetails details = new PlaceDetails(placeResult.name(), placeResult.vicinity(),
                        placeResult.rating(), weekdayText, wikipediaText, placeResult.types());
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
