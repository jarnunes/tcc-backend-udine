package com.pucminas.integrations.udine.vo;

import java.io.Serializable;
import java.util.List;

public record PlaceDetails(String name, String id, String address, Double ranting, List<String> opening_hours,
    String wikipediaDescription, List<String> locationTypes) implements Serializable {
}
