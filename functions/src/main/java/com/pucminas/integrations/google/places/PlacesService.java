package com.pucminas.integrations.google.places;

import com.pucminas.integrations.google.places.dto.*;

import java.util.List;

public interface PlacesService {

    // https://developers.google.com/maps/documentation/places/web-service/reference/rpc/google.maps.places.v1?hl=pt-br
    // https://developers.google.com/maps/documentation/places/web-service/place-details?hl=pt-br
    // https://developers.google.com/maps/documentation/places/web-service/nearby-search?hl=pt-br
    PlacesResponse searchNearby(PlacesSearchNearbyRequest request);
    PlacesResponse searchByText(PlacesSearchTextRequest request);

    void sortPlacesByRanting(List<Place> places);
    void sortPlacesByDistance(List<Place> places);
    void complementWithDistance(List<Place> places, Location location);
    void complementWithCityName(List<Place> places);

    // https://developers.google.com/maps/documentation/places/web-service/details?hl=pt-br
    Place getPlaceDetails(String placeId);

    List<Place> getPlacesDetails(List<String> placesId);

    Photo getPlacePhoto(String photoReference);

    List<Photo> getPlacePhotos(List<String> photoReferences);

    List<Place> searchByText(QuestionDefinition questionDefinition, Location location);

}
