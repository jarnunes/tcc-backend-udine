package com.pucminas.integrations.google.speech_to_text.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alternative {
    private String transcript;
    private Double confidence;
}
