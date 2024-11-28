package com.pucminas.api.dto;

import com.pucminas.integrations.google.places.dto.Place;
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
public class NearbyPlaceDescriptionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -543598688975697385L;

    private List<Place> places = new ArrayList<>();
    private String audioDescriptionContent;
}
