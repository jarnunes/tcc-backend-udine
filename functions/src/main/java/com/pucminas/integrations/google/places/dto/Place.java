package com.pucminas.integrations.google.places.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    String name;

    String vicinity;

    @JsonProperty("place_id")
    String placeId;

    List<String> types = new ArrayList<>();

    Geometry geometry;

    @JsonProperty("uri_icon")
    String uriIcon;
}