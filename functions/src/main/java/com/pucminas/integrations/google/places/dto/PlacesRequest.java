package com.pucminas.integrations.google.places.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlacesRequest {

    Double latitude;
    Double longitude;
    Integer radius;
    String type = "tourist_attraction";

}
