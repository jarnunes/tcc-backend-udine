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

    private String name;
    private String vicinity;

    @JsonProperty("place_id")
    private String placeId;

    private List<String> types = new ArrayList<>();
    private Geometry geometry;

    @JsonProperty("icon")
    private String uriIcon;

}