package com.pucminas.function.functions;

import com.pucminas.integrations.google.tech_to_speech.TextToSpeechService;
import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechResponse;
import com.pucminas.integrations.google.vertex.VertexAIService;
import com.pucminas.integrations.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class GenerateShortAudioDescriptionFromPlacesName implements Function<List<String>, TextToSpeechResponse> {


    private VertexAIService vertexAIService;
    private TextToSpeechService textToSpeechService;
    private OpenAiService openAiService;

    @Autowired
    public void setVertexAIService(VertexAIService service) {
        this.vertexAIService = service;
    }

    @Autowired
    public void setTextToSpeechService(TextToSpeechService service) {
        this.textToSpeechService = service;
    }

    @Autowired
    public void setOpenAiService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public TextToSpeechResponse apply(List<String> placesNames) {
        final String description = openAiService.generateShortDescription(placesNames);
        return textToSpeechService.synthesizeText(description);
    }
}
