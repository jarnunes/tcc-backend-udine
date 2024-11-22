package com.pucminas.integrations.google.speech_to_text.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Config {
    private AudioEncodingType encoding;
    private Integer sampleRateHertz;
    private String languageCode;
    private Boolean enableWordTimeOffsets;
    private Boolean enableAutomaticPunctuation;

    public Config() {
        this.encoding = AudioEncodingType.OGG_OPUS;
        this.sampleRateHertz = 16000;
        this.languageCode = "pt-BR";
        this.enableWordTimeOffsets = false;
        this.enableAutomaticPunctuation = true;
    }
}
