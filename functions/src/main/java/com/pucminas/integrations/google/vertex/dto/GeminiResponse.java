package com.pucminas.integrations.google.vertex.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponse {

    private List<GeminiCandidate> candidates = new ArrayList<>();

    public void addCandidate(GeminiCandidate candidate) {
        if (this.candidates == null) {
            this.candidates = new ArrayList<>();
        }
        this.candidates.add(candidate);
    }

    public static GeminiResponse create(String responseText) {
        final GeminiContent content = new GeminiContent();
        content.addPart(new GeminiPart(responseText));

        final GeminiCandidate candidate = new GeminiCandidate();
        candidate.setContent(content);

        final GeminiResponse response = new GeminiResponse();
        response.addCandidate(candidate);
        return response;
    }
}
