package com.pucminas.integrations.google.places.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHours implements Serializable {

    @Serial
    private static final long serialVersionUID = -884944325743533726L;

    @JsonProperty("weekday_text")
    private List<String> weekdayText = new ArrayList<>();
}
