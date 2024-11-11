package com.pucminas.functionlayer.integrations.google.vertex;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "udine-tour.vertex")
public class VertexProperties {

    @Value("${udine-tour.vertex.gemini.url}")
    private String geminiUrl;

    @Value("${udine-tour.vertex.gemini.api.key}")
    private String geminiApiKey;

    @Value("${udine-tour.vertex.gemini.api.param.api-key:key}")
    private String geminiApiNameParamApiKey;

    public String getGeminiUrl() {
        final StringBuilder geminiUrl = new StringBuilder(this.geminiUrl);
        geminiUrl.append("?").append(this.geminiApiNameParamApiKey).append("=");
        geminiUrl.append(this.geminiApiKey);
        return geminiUrl.toString();
    }
}
