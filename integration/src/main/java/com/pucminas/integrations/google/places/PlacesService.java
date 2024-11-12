package com.pucminas.integrations.google.places;

import com.pucminas.integrations.google.places.dto.PlacesRequest;
import com.pucminas.integrations.google.places.dto.PlacesResponse;

public interface PlacesService {

    PlacesResponse getNearbyPlaces(PlacesRequest request);
}
