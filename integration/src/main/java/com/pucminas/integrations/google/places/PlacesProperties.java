package com.pucminas.integrations.google.places;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "udine.google.places")
public class PlacesProperties {

    @Value("${udine.google.places.url}")
    private String googlePlacesURL;

    @Value("${udine.google.places.apiKey}")
    private String googlePlacesApiKey;

}
