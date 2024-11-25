package com.pucminas.integrations.google.places.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlaceRequestRestrictionCenter implements Serializable {

    @Serial
    private static final long serialVersionUID = -6009281159093132716L;

    private Double latitude;
    private Double longitude;
}
