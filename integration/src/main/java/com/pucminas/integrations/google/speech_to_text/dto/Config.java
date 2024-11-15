package com.pucminas.integrations.google.speech_to_text.dto;

import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Config {
    private RecognitionConfig.AudioEncoding encoding;
    private Integer sampleRateHertz;
    private String languageCode;
    private Boolean enableWordTimeOffsets;

    public Config() {
        this.encoding = RecognitionConfig.AudioEncoding.OGG_OPUS;
        this.sampleRateHertz = 16000;
        this.languageCode = "pt-BR";
        this.enableWordTimeOffsets = false;
    }
}
