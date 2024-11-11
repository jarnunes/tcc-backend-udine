package com.pucminas.functionlayer.integrations.google.vertex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiCandidate {

    private GeminiContent content;
}
