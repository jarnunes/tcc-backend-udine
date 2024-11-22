package com.pucminas.integrations.google.places.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDetailResult  implements Serializable {

    @Serial
    private static final long serialVersionUID = 2689572094696848085L;

    private String name;
    private Double rating;
    private String vicinity;
    private String url;
    private String website;
    @JsonProperty("opening_hours")
    private OpeningHours openingHours;
    private List<String> types;
    private Geometry geometry;
    private String city;
}