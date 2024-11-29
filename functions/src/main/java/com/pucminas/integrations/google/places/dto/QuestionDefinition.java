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
public class QuestionDefinition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1241924373083223788L;

    private ClassificationType classification;
    private LocationType locationType;
    private String textQuery;
    private boolean showPhotos;

    public String locationType() {
        return locationType.getValue();
    }
}
