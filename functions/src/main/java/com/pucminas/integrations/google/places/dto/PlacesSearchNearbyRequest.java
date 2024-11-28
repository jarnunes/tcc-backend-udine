package com.pucminas.integrations.google.places.dto;

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
public class PlacesSearchNearbyRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -472297264770491541L;
    private PlaceRequestRestriction locationRestriction;
    private List<String> includedTypes = new ArrayList<>();

    public void addIncludedType(String type) {
        this.includedTypes.add(type);
    }
}
