package com.pucminas.integrations.google.places.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ApiType {

    NONE("empty"),
    SEARCH_TEXT("searchText"),
    SEARCH_NEARBY("searchNearby");

    @JsonValue
    private final String value;

    ApiType(String value) {
        this.value = value;
    }

    public boolean isSearchText() {
        return this.equals(SEARCH_TEXT);
    }

    public boolean isSearchNearby() {
        return this.equals(SEARCH_NEARBY);
    }

    public boolean isNone() {
        return this.equals(NONE);
    }
}
