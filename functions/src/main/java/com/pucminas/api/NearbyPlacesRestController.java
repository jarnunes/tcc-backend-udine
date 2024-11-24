package com.pucminas.api;

import com.pucminas.integrations.google.places.PlacesService;
import com.pucminas.integrations.google.places.dto.PlacesRequest;
import com.pucminas.integrations.google.places.dto.PlacesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nearbyPlaces")
public class NearbyPlacesRestController extends RestControllerBase {

    private PlacesService placesService;

    @Autowired
    public void setPlacesService(PlacesService service) {
        this.placesService = service;
    }

    @PostMapping
    public ResponseEntity<PlacesResponse> apply(@RequestBody PlacesRequest request) {
        return ResponseEntity.ok(super.processRequest(request, placesService::getNearbyPlaces));
    }

    @Override
    protected String serviceName() {
        return "NearbyPlacesRestController";
    }
}
