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
public class Review implements Serializable {

    @Serial
    private static final long serialVersionUID = 3177597300543103543L;

    private String relativePublishTimeDescription;
    private Double ranting;
    private PlaceText text;
}
