package com.pucminas.integrations.openai;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "udine.openai")
public class OpenAiProperties {

    @Value("${udine.openai.url}")
    private String url;

    @Value("${udine.openai.api-key}")
    private String apiKey;
}
