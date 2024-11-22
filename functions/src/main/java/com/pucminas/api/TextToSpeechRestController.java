package com.pucminas.api;

import com.pucminas.integrations.google.tech_to_speech.TextToSpeechService;
import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/textToSpeech")
public class TextToSpeechRestController extends FunctionBase {

    private TextToSpeechService service;

    @Autowired
    public void setTextToSpeechService(TextToSpeechService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TextToSpeechResponse> apply(String text) {
        return ResponseEntity.ok(processRequest(text, service::synthesizeText));
    }

    @Override
    protected String serviceName() {
        return "TextToSpeechRestController";
    }
}
