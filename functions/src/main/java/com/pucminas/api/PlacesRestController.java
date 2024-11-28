package com.pucminas.api;

import com.pucminas.api.dto.NearbyPlaceDescriptionResponse;
import com.pucminas.commons.resource.FileResource;
import com.pucminas.integrations.google.places.PlacesService;
import com.pucminas.integrations.google.places.dto.Place;
import com.pucminas.integrations.google.places.dto.PlacesSearchNearbyRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/places")
public class PlacesRestController extends RestControllerBase {

    private PlacesService placesService;

    @Autowired
    public void setPlacesService(PlacesService service) {
        this.placesService = service;
    }

    @Override
    protected String serviceName() {
        return "PlacesRestController";
    }

    @PostMapping("/searchNearby")
    public ResponseEntity<NearbyPlaceDescriptionResponse> searchNearby(@RequestBody PlacesSearchNearbyRequest request) {
        final var placesResponse = super.processRequest(request, placesService::searchNearby);
        return placesResponse == null
            ? createResponse(List.of())
            : createResponse(placesResponse.getPlaces());
    }

    private ResponseEntity<NearbyPlaceDescriptionResponse> createResponse(List<Place> places) {
        final var response = new NearbyPlaceDescriptionResponse();
        response.setPlaces(places);
        response.setAudioDescriptionContent(getShortAudioDescription(CollectionUtils.isNotEmpty(places)));
        return ResponseEntity.ok(response);
    }

    private String getShortAudioDescription(boolean locationsFound) {
        return FileResource.instance().readDefaultFile(locationsFound
            ? "audio_introducao_locais_encontrados.txt"
            : "audio_introducao_sem_locais_proximos.txt");
    }

}
