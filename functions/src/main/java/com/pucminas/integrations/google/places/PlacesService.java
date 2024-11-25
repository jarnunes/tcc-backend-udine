package com.pucminas.integrations.google.places;

import com.pucminas.integrations.google.places.dto.PlaceDetailResponse;
import com.pucminas.integrations.google.places.dto.PlacesRequest;
import com.pucminas.integrations.google.places.dto.PlacesResponse;

import java.util.List;

public interface PlacesService {

    // https://developers.google.com/maps/documentation/places/web-service/reference/rpc/google.maps.places.v1?hl=pt-br
    // https://developers.google.com/maps/documentation/places/web-service/place-details?hl=pt-br
    PlacesResponse getNearbyPlaces(PlacesRequest request);

    PlacesResponse getNearbyPlaces(PlacesRequest request, List<String> types);

    // https://developers.google.com/maps/documentation/places/web-service/details?hl=pt-br
    PlaceDetailResponse getPlaceDetails(String placeId);

    List<PlaceDetailResponse> getPlacesDetails(List<String> placesId);
}
