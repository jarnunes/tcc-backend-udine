package com.pucminas.function.functions;

import com.pucminas.integrations.google.places.PlacesService;
import com.pucminas.integrations.google.places.dto.PlacesRequest;
import com.pucminas.integrations.google.places.dto.PlacesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

//@Component
public class NearbyPlaces extends FunctionBase implements Function<PlacesRequest, PlacesResponse> {

    private PlacesService placesService;

//    @Autowired
    public void setPlacesService(PlacesService service) {
        this.placesService = service;
    }

    @Override
    public PlacesResponse apply(PlacesRequest request) {
        return super.processRequest(request, placesService::getNearbyPlaces);
    }

    @Override
    protected String serviceName() {
        return "NearbyPlaces";
    }
}
