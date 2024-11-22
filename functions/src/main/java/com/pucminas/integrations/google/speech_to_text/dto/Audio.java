package com.pucminas.integrations.google.speech_to_text.dto;

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
public class Audio implements Serializable {

    @Serial
    private static final long serialVersionUID = -3047804994386809363L;

    private String content;
}
