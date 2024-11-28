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
public class PlacesSearchTextRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -472297264770491541L;
    private PlaceRequestRestriction locationBias;
    private String textQuery;

}
