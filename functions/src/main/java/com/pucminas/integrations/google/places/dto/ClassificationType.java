package com.pucminas.integrations.google.places.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ClassificationType {

    EMPTY("empty"),
    DISTANCE("distance"),
    POPULARITY("popularity");

    @JsonValue
    private final String value;

    ClassificationType(String value) {
        this.value = value;
    }

    public boolean isDistance() {
        return this.equals(DISTANCE);
    }
}
