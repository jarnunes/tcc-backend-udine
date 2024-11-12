package com.pucminas.integrations.google.places;

import com.pucminas.integrations.google.places.dto.PlacesRequest;
import com.pucminas.integrations.google.places.dto.PlacesResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@CommonsLog
public class PlacesServiceImpl implements PlacesService {
    private final RestTemplate restTemplate = new RestTemplate();
    private PlacesProperties properties;

    @Autowired
    public void setProperties(PlacesProperties properties) {
        this.properties = properties;
    }

    public PlacesResponse getNearbyPlaces(PlacesRequest request) {
        final String url = UriComponentsBuilder.fromHttpUrl(properties.getGooglePlacesURL())
                .queryParam("location", request.getLatitude() + "," + request.getLongitude())
                .queryParam("radius", request.getRadius())
                .queryParam("key", properties.getGooglePlacesApiKey())
                .toUriString();

        return restTemplate.getForObject(url, PlacesResponse.class);
    }
}
