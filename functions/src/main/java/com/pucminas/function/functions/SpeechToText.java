package com.pucminas.function.functions;

import com.pucminas.integrations.google.speech_to_text.SpeechToTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

//@Component
public class SpeechToText extends FunctionBase implements UnaryOperator<String> {

    private SpeechToTextService service;

//    @Autowired
    public void setService(SpeechToTextService service) {
        this.service = service;
    }

    @Override
    public String apply(String audio) {
        return processRequest(audio, service::recognizeAudioMP3);
    }

    @Override
    protected String serviceName() {
        return "SpeechToText";
    }
}
