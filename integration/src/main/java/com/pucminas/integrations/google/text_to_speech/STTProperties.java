package com.pucminas.integrations.google.text_to_speech;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "udine.speech-to-text")
public class STTProperties {

    @Value("${udine.speech-to-text.base-uri}")
    private String baseURI;

    @Value("${udine.speech-to-text.path}")
    private String path;

    @Value("${udine.speech-to-text.apiKey}")
    private String apiKey;

}
