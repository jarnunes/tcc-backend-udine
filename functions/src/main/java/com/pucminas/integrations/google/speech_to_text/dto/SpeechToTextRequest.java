package com.pucminas.integrations.google.speech_to_text.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class SpeechToTextRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8086757446613667604L;

    private Config config;
    private Audio audio;

    public SpeechToTextRequest(String audio) {
        this.config = new Config();
        this.audio = new Audio(audio);
    }
}
