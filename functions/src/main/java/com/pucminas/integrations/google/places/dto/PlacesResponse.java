package com.pucminas.integrations.google.places.dto;

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
public class PlacesResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 4199741868715763761L;

    List<Place> results = new ArrayList<>();
}
