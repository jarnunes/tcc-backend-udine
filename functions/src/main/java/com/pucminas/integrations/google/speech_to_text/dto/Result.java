package com.pucminas.integrations.google.speech_to_text.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private List<Alternative> alternatives = new ArrayList<>();
    private String resultEndTime;
    private String languageCode;
}
