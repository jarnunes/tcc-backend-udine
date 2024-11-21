package com.pucminas.integrations.google.geocode;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "udine.geocode")
public class GeocodeProperties {

    @Value("${udine.geocode.url}")
    private String url;

    @Value("${udine.geocode.api-key}")
    private String apiKey;
}
