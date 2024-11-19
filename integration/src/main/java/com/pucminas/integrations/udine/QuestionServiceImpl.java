package com.pucminas.integrations.udine;

import com.pucminas.integrations.google.places.PlacesService;
import com.pucminas.integrations.google.places.dto.PlaceDetailResponse;
import com.pucminas.integrations.google.speech_to_text.SpeechToTextService;
import com.pucminas.integrations.google.tech_to_speech.TextToSpeechService;
import com.pucminas.integrations.google.vertex.VertexAIService;
import com.pucminas.integrations.udine.vo.QuestionFormatType;
import com.pucminas.integrations.udine.vo.QuestionRequest;
import com.pucminas.integrations.udine.vo.QuestionResponse;
import com.pucminas.utils.JsonUtils;
import com.pucminas.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionServiceImpl implements QuestionService {

    private PlacesService placesService;
    private VertexAIService vertexAIService;
    private SpeechToTextService speechToTextService;
    private TextToSpeechService textToSpeechService;

    @Autowired
    public void setPlacesService(PlacesService placesService) {
        this.placesService = placesService;
    }

    @Autowired
    public void setVertexAIService(VertexAIService service) {
        this.vertexAIService = service;
    }

    @Autowired
    public void setSpeechToTextService(SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    @Autowired
    public void setTextToSpeechService(TextToSpeechService textToSpeechService) {
        this.textToSpeechService = textToSpeechService;
    }

    @Override
    public QuestionResponse answerQuestion(QuestionRequest questionRequest) {
        final String question = getUserQuestion(questionRequest);
        final List<PlaceDetailResponse> placesDetails = placesService.getPlacesDetails(questionRequest.placesId());
        final String prompt = MessageUtils.get("questions.locations.prompt", JsonUtils.toJsonString(placesDetails), question);
        final String answered = vertexAIService.answerQuestion(prompt);

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
