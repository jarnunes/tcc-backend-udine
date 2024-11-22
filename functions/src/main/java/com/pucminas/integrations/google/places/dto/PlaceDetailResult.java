package com.pucminas.integrations.google.places.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDetailResult {
    String name;
    Double rating;
    String vicinity;
    String url;
    String website;
    @JsonProperty("opening_hours")
    OpeningHours openingHours;
    List<String> types;
    Geometry geometry;
    String city;
}
