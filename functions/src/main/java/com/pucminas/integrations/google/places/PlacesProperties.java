package com.pucminas.integrations.google.places;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@ConfigurationProperties(prefix = "udine.google.places")
public class PlacesProperties {

    @Value("${udine.google.places.url}")
    private String url;

    @Value("${udine.google.places.nearby-search-path}")
    private String nearbySearchPath;

    @Value("${udine.google.places.apiKey}")
    private String googlePlacesApiKey;

    private List<String> defaulttypes = new ArrayList<>();

    private List<String> types = new ArrayList<>();

    @Value("${udine.google.places.radius}")
    private Double radius;

    @Value("${udine.google.places.max-results}")
    private Integer maxResults;


}
