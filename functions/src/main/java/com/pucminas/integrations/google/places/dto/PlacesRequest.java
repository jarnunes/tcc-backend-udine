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
public class PlacesRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -472297264770491541L;

    Double latitude;
    Double longitude;
    Integer radius;
    String type = "tourist_attraction";

}
