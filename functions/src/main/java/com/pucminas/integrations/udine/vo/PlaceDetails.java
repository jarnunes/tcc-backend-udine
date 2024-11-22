package com.pucminas.integrations.udine.vo;

import java.util.List;

public record PlaceDetails(String name, String address, Double ranting, List<String> opening_hours,
    String wikipediaDescription, List<String> locationTypes) {
}
