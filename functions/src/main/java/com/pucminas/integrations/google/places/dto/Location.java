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
public class Location implements Serializable {

    @Serial
    private static final long serialVersionUID = -3558468037386032571L;

    Double lat;
    Double lng;
}
