package com.pucminas.integrations.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pucminas.integrations.openai.vo.MessageRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@ConfigurationProperties(prefix = "udine.openai")
public class OpenAiProperties {

    @Value("${udine.openai.url}")
    private String url;

    @Value("${udine.openai.path}")
    private String path;

    @Value("${udine.openai.api-key}")
    private String apiKey;

    @Value("${udine.openai.model}")
    private String model;

    @Value("${udine.openai.max-tokens}")
    private Integer maxTokens;

    @Value("${udine.openai.temperature}")
    private Double temperature;

    @Value("${udine.openai.create-fake-text}")
    private boolean createFakeText  = false;

    @Value("${udine.openai.fake-text}")
    private String fakeText;
}
