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
public class PlaceRequestRestrictionCircle implements Serializable {

    @Serial
    private static final long serialVersionUID = 6570700492827260839L;

    private PlaceRequestRestrictionCenter center;
    private Double radius;
}
