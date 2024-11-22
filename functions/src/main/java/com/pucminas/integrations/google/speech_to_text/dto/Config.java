package com.pucminas.integrations.google.speech_to_text.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Config implements Serializable {

    @Serial
    private static final long serialVersionUID = 3999146952377829095L;

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
