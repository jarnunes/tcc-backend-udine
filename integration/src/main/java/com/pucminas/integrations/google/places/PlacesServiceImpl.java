package com.pucminas.integrations.google.places;

import com.pucminas.integrations.google.places.dto.PlaceDetailResponse;
import com.pucminas.integrations.google.places.dto.PlacesRequest;
import com.pucminas.integrations.google.places.dto.PlacesResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
@CommonsLog
public class PlacesServiceImpl implements PlacesService {
    private PlacesProperties properties;


    @Autowired
    public void setProperties(PlacesProperties properties) {
        this.properties = properties;
    }

    @Override
    public PlacesResponse getNearbyPlaces(PlacesRequest request) {
        return WebClient.builder()
                .baseUrl(properties.getUrl())
                .build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(properties.getNearbySearchPath())
                        .queryParam("location", request.getLatitude() + "," + request.getLongitude())
                        .queryParam("radius", request.getRadius())
                        .queryParam("type", request.getType())
                        .queryParam("key", properties.getGooglePlacesApiKey())
                        .build())
                .header("Accept-Language", "pt")
                .retrieve()
                .bodyToMono(PlacesResponse.class)
                .block();
    }

    @Override
    public PlaceDetailResponse getPlaceDetails(String placeId) {
        return WebClient.builder()
                .baseUrl(properties.getUrl())
                .build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(properties.getDetailsPath())
                        .queryParam("place_id", placeId)
                        .queryParam("key", properties.getGooglePlacesApiKey())
                        .build())
                .header("Accept-Language", "pt")
                .retrieve()
                .bodyToMono(PlaceDetailResponse.class)
                .block();
    }

    @Override
    public List<PlaceDetailResponse> getPlacesDetails(List<String> placesId) {
        List<PlaceDetailResponse> responses = new ArrayList<>();
        placesId.parallelStream().map(this::getPlaceDetails).forEach(responses::add);
        return responses;
    }

}
