package com.pucminas.integrations;

import com.pucminas.integrations.google.places.dto.Place;

import java.util.List;

public interface LlmService {

    String generateShortDescription(List<String> locationsName);
    String generateShortDescriptionFromPlaces(List<Place> places);

    String generateShortDescription(String locationName);

    String answerQuestion(String questionPrompt);
}
