package com.pucminas.integrations.google.places.dto;

import java.util.List;

public record PlaceDetailResult(String name, Double rating, String vicinity, String url, String website,
    OpeningHours opening_hours, List<String> types) {
}
