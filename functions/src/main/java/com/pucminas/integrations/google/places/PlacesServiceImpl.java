package com.pucminas.integrations.google.places;

import com.pucminas.commons.utils.StrUtils;
import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.google.geocode.GeocodeService;
import com.pucminas.integrations.google.places.dto.Location;
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
public class PlacesServiceImpl extends ServiceBase implements PlacesService {
    private PlacesProperties properties;
    private GeocodeService geocodeService;

    @Autowired
    public void setProperties(PlacesProperties properties) {
        this.properties = properties;
    }

    @Autowired
    public void setGeocodeService(GeocodeService geocodeService) {
        this.geocodeService = geocodeService;
    }

    @Override
    protected String serviceNameKey() {
        return "google.places.service.name";
    }

    @Override
    public PlacesResponse getNearbyPlaces(PlacesRequest request) {
        return cacheService.getCachedValueOrNew(getClass(), "KEY_PLACES_SERVICE_NEARBY_PLACES", request,
            this::getNearbyPlacesLocation);
    }

    private PlacesResponse getNearbyPlacesLocation(PlacesRequest request) {
        return processWithAttempts(3, request, () -> WebClient.builder()
                .baseUrl(properties.getUrl())
                .build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(properties.getNearbySearchPath())
                        .queryParam("location", StrUtils.joinObjects(request.getLatitude(), request.getLongitude()))
                        .queryParam("radius", request.getRadius())
                        .queryParam("type", request.getType())
                        .queryParam("key", properties.getGooglePlacesApiKey())
                        .build())
                .header("Accept-Language", "pt")
                .retrieve()
                .bodyToMono(PlacesResponse.class)
                .block());
    }

    @Override
    public PlaceDetailResponse getPlaceDetails(String placeId) {
        return cacheService.getCachedValueOrNew(getClass(), "KEY_PLACES_SERVICE_PLACES_DETAILS", placeId,
                this::findPlaceDetailsById);
    }

    private PlaceDetailResponse findPlaceDetailsById(String placeID) {
        return processWithAttempts(3, placeID, () -> {
            final PlaceDetailResponse response = WebClient.builder()
                    .baseUrl(properties.getUrl())
                    .build().get()
                    .uri(uriBuilder -> uriBuilder
                            .path(properties.getDetailsPath())
                            .queryParam("place_id", placeID)
                            .queryParam("key", properties.getGooglePlacesApiKey())
                            .build())
                    .header("Accept-Language", "pt")
                    .retrieve()
                    .bodyToMono(PlaceDetailResponse.class)
                    .block();

            if (response != null) {
                final Location location = response.getResult().getGeometry().getLocation();
                final String city = geocodeService.getCityName(location.getLat(), location.getLng());
                response.getResult().setCity(city);
            }
            return response;
        });
    }

    @Override
    public List<PlaceDetailResponse> getPlacesDetails(List<String> placesId) {
        List<PlaceDetailResponse> responses = new ArrayList<>();
        placesId.parallelStream().map(this::getPlaceDetails).forEach(responses::add);
        return responses;
    }

}
