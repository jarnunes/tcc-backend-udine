package com.pucminas.integrations.google.tech_to_speech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TextToSpeechResponse  implements Serializable {

    @Serial
    private static final long serialVersionUID = 3176339774012120846L;

    private String audioContent;
}
