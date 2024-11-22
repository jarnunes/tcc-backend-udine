package com.pucminas.integrations.google.speech_to_text.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SpeechToTextRequest {

    private Config config;
    private Audio audio;

    public SpeechToTextRequest(String audio) {
        this.config = new Config();
        this.audio = new Audio(audio);
    }
}
