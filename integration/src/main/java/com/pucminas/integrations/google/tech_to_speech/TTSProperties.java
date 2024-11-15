package com.pucminas.integrations.google.tech_to_speech;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "udine.text-to-speech")
public class TTSProperties {


    @Value("${udine.text-to-speech.base-uri}")
    private String baseURI;

    @Value("${udine.text-to-speech.path}")
    private String path;

    @Value("${udine.text-to-speech.apiKey}")
    private String apiKey;

}
