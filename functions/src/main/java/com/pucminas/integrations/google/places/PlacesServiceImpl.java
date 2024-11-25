package com.pucminas.integrations.google.places;

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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
        request.setIncludedTypes(properties.getTypes());

        return cacheService.getCachedValueOrNew(getClass(), "KEY_PLACES_SERVICE_NEARBY_PLACES", request,
            this::getNearbyPlacesLocation);
    }

    @Override
    public PlacesResponse getNearbyPlaces(PlacesRequest request, List<String> types) {
        request.setIncludedTypes(types);
        return getNearbyPlacesLocation(request);
    }

    private PlacesResponse getNearbyPlacesLocation(PlacesRequest request) {
        final PlacesResponse response = new PlacesResponse();
        final AtomicReference<String> nextPageToken = new AtomicReference<>();

        processWithAttempts(3, request, () -> {
            do {
                final PlacesResponse currentPageResponse = getBuilder()
                        .baseUrl(properties.getUrl())
                        .build().post()
                        .uri(uriBuilder -> uriBuilder
                                .path(properties.getNearbySearchPath())
                                .queryParamIfPresent("pagetoken", Optional.ofNullable(nextPageToken.get()))
                                .build())
                        .header("Accept-Language", "pt")
                        .header("X-Goog-Api-Key", properties.getGooglePlacesApiKey())
                        .header("X-Goog-FieldMask","*")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(PlacesResponse.class)
                        .block();

                if (currentPageResponse != null && response.getPlaces() != null) {
                    response.getPlaces().addAll(currentPageResponse.getPlaces());
                    nextPageToken.set(currentPageResponse.getNextPageToken());
                    waitValidToken(currentPageResponse.getNextPageToken());
                } else {
                    nextPageToken.set(null);
                }
            } while (response.getPlaces().size() < properties.getMaxResults() && nextPageToken.get() != null);

            return null;
        });

        return response;
    }

    private void waitValidToken(String token) {
        if (token != null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
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
                final String city = geocodeService.getCityName(location.getLatitude(), location.getLongitude());
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
