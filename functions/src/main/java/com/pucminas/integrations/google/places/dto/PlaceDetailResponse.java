package com.pucminas.integrations.google.places.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDetailResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 8886977529677667466L;

    private PlaceDetailResult result;
}
