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
public class QuestionApiUsage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1241924373083223788L;

    private ApiType api;
    private ClassificationType classification;
    private LocationType locationType;
    private String textQuery;
    private Boolean showPhotos;

    public String locationType() {
        return locationType.getValue();
    }
}
