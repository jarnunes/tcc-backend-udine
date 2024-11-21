package com.pucminas.function.functions;

import com.pucminas.integrations.google.tech_to_speech.TextToSpeechService;
import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechResponse;
import com.pucminas.integrations.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

//@Component
public class GenerateShortAudioDescriptionFromPlacesName extends FunctionBase
    implements Function<List<String>, TextToSpeechResponse> {

    private TextToSpeechService textToSpeechService;
    private OpenAiService openAiService;

//    @Autowired
    public void setTextToSpeechService(TextToSpeechService service) {
        this.textToSpeechService = service;
    }

//    @Autowired
    public void setOpenAiService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public TextToSpeechResponse apply(List<String> placesNames) {
        final String description = super.processRequest(placesNames, openAiService::generateShortDescription);
        return super.processRequest(description, textToSpeechService::synthesizeText);
    }

    @Override
    protected String serviceName() {
        return "GenerateShortAudioDescriptionFromPlacesName";
    }
}
