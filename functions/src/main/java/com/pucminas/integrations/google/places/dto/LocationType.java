package com.pucminas.integrations.google.places.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum LocationType {

    EMPTY("empty"),
    RESTAURANT("restaurant"),
    HOTEL("hotel"),
    TOURIST_ATTRACTION("tourist_attraction");

    @JsonValue
    private final String value;

    LocationType(String value) {
        this.value = value;
    }

    public boolean isRestaurant() {
        return this.equals(RESTAURANT);
    }

    public boolean isHotel() {
        return this.equals(HOTEL);
    }

    public boolean isTouristAttraction() {
        return this.equals(TOURIST_ATTRACTION);
    }
}


