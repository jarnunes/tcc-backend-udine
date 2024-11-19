package com.pucminas.function.functions;

import com.pucminas.integrations.google.speech_to_text.SpeechToTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SpeechToText implements Function<String, String> {

    private SpeechToTextService service;

    @Autowired
    public void setService(SpeechToTextService service) {
        this.service = service;
    }

    @Override
    public String apply(String audio) {
        return service.recognizeAudioOGG(audio);
    }
}
