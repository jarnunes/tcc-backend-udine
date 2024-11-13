package com.pucminas.integrations.google.text_to_speech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TextToSpeechResponse {
    private String audioContent;
}
