package com.pucminas.integrations.google.places.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlacesResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 4199741868715763761L;

    @JsonProperty("next_page_token")
    String nextPageToken;

    List<Place> places = new ArrayList<>();

    public List<Place> getPlaces() {
        if (places == null) {
            places = new ArrayList<>();
        }
        return places;
    }

    public void addPlaces(List<Place> places) {
        getPlaces().addAll(places);
    }
}
