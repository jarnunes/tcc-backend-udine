package com.pucminas.integrations.google.places;

import com.pucminas.integrations.google.places.dto.PlaceDetailResponse;
import com.pucminas.integrations.google.places.dto.PlacesRequest;
import com.pucminas.integrations.google.places.dto.PlacesResponse;

import java.util.List;

public interface PlacesService {

    PlacesResponse getNearbyPlaces(PlacesRequest request);

    // https://developers.google.com/maps/documentation/places/web-service/details?hl=pt-br
    PlaceDetailResponse getPlaceDetails(String placeId);

    List<PlaceDetailResponse> getPlacesDetails(List<String> placesId);
}
