package com.pucminas.api;

import com.pucminas.integrations.google.speech_to_text.SpeechToTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/speechToText")
public class SpeechToTextRestController extends FunctionBase {

    private SpeechToTextService service;

    @Autowired
    public void setService(SpeechToTextService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> apply(@RequestBody String audio) {
        return ResponseEntity.ok(processRequest(audio, service::recognizeAudioMP3));
    }

    @Override
    protected String serviceName() {
        return "SpeechToTextRestController";
    }
}
