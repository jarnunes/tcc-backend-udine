package com.pucminas.integrations.google.places;

import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.google.geocode.GeocodeService;
import com.pucminas.integrations.google.places.dto.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
@CommonsLog
public class PlacesServiceImpl extends ServiceBase implements PlacesService {
    private static final String KEY_PLACES_SERVICE_PLACES_DETAILS = "KEY_PLACES_SERVICE_PLACES_DETAILS";

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

        final PlaceRequestRestrictionCircle circle = request.getLocationRestriction().getCircle();
        if(circle.getRadius() == null) {
            circle.setRadius(properties.getRadius());
        }

        return cacheService.getCachedValueOrNew(getClass(), "KEY_PLACES_SERVICE_NEARBY_PLACES", request,
            this::getNearbyPlacesLocation);
    }

    @Override
    public PlacesResponse getNearbyPlaces(PlacesRequest request, List<String> types) {
        request.setIncludedTypes(types);
        return getNearbyPlacesLocation(request);
    }

    private PlacesResponse getNearbyPlacesLocation(PlacesRequest request) {
        final Set<String> includedTypes = new HashSet<>(request.getIncludedTypes());
        final PlacesResponse response = new PlacesResponse();
        final AtomicReference<String> nextPageToken = new AtomicReference<>();

        // https://stackoverflow.com/questions/77898813/next-page-token-for-new-google-maps-places-api-nearbysearch-pagination
        for(String placeType : includedTypes){
            request.setIncludedTypes(Collections.singletonList(placeType));
            final PlacesResponse responseByType = processWithAttempts(3, request, () -> getBuilder()
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
                    .block());

            response.getPlaces().parallelStream().forEach(place -> {
                final String city = geocodeService.getCityName(place.getLocation().getLatitude(), place.getLocation().getLongitude());
                place.setCity(city);
            });

            response.addPlaces(responseByType.getPlaces());
        }

        for(Place place : response.getPlaces()) {
            cacheService.putCache(getClass(), KEY_PLACES_SERVICE_PLACES_DETAILS, place.getId(), place);
        }

        return response;
    }

    @Override
    public Place getPlaceDetails(String placeId) {
        return cacheService.getCachedValueOrNew(getClass(), KEY_PLACES_SERVICE_PLACES_DETAILS, placeId,
            this::findPlaceDetailsById);
    }

    private Place findPlaceDetailsById(String placeID) {
        return processWithAttempts(3, placeID, () -> {
            final Place placeDetails = getBuilder()
                    .baseUrl(properties.getUrl())
                    .build().get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/" + placeID)
                            .build())
                    .header("Accept-Language", "pt")
                    .header("X-Goog-Api-Key", properties.getGooglePlacesApiKey())
                    .header("X-Goog-FieldMask","*")
                    .retrieve()
                    .bodyToMono(Place.class)
                    .block();

            if (placeDetails != null) {
                final Location location = placeDetails.getLocation();
                final String city = geocodeService.getCityName(location.getLatitude(), location.getLongitude());
                placeDetails.setCity(city);
            }
            return placeDetails;
        });
    }

    @Override
    public List<Place> getPlacesDetails(List<String> placesId) {
        List<Place> responses = new ArrayList<>();
        placesId.parallelStream().map(this::getPlaceDetails).forEach(responses::add);
        return responses;
    }

}
