package com.pucminas.integrations.google.vertex;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "udine.vertex")
public class VertexProperties {

    @Value("${udine.vertex.gemini.url}")
    private String geminiUrl;

    @Value("${udine.vertex.gemini.api-key}")
    private String geminiApiKey;

    @Value("${udine.vertex.gemini.name.param.apiKey:key}")
    private String geminiApiNameParamApiKey;

    @Value("${udine.vertex.gemini.connections-attempt}")
    private Integer connectionsAttempt = 3;

    public String getGeminiUrl() {
        final StringBuilder geminiUrl = new StringBuilder(this.geminiUrl);
        geminiUrl.append("?").append(this.geminiApiNameParamApiKey).append("=");
        geminiUrl.append(this.geminiApiKey);
        return geminiUrl.toString();
    }
}
