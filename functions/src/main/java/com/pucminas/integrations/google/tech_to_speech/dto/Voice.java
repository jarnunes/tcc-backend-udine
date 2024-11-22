package com.pucminas.integrations.google.tech_to_speech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Voice {

    private String languageCode;
    private String name;
    private String ssmlGender;
}
