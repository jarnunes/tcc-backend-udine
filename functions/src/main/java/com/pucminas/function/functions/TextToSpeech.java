package com.pucminas.function.functions;

import com.pucminas.integrations.google.tech_to_speech.TextToSpeechService;
import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

//@Component
public class TextToSpeech extends FunctionBase implements Function<String, TextToSpeechResponse> {

    private TextToSpeechService service;

//    @Autowired
    public void setTextToSpeechService(TextToSpeechService service) {
        this.service = service;
    }

    @Override
    public TextToSpeechResponse apply(String text) {
        return processRequest(text, service::synthesizeText);
    }

    @Override
    protected String serviceName() {
        return "TextToSpeech";
    }
}
