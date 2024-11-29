package com.pucminas.integrations.google.places.dto;

import com.pucminas.integrations.wikipedia.dto.SearchLike;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlaceInWikipedia implements Serializable {
    @Serial
    private static final long serialVersionUID = -2313669368680140373L;
    private String id;
    private String locationName;
    private String city;
    private String title;
    private List<SearchLike> suggestedTitles = new ArrayList<>();


    public PlaceInWikipedia(String id, String locationName, String city, List<SearchLike> suggestedTitles) {
        this.id = id;
        this.locationName = locationName;
        this.city = city;
        this.suggestedTitles = suggestedTitles;
    }
}
