package com.pucminas.integrations.google.vertex;

import com.pucminas.integrations.google.vertex.dto.GeminiResponse;

import java.util.List;

public interface VertexAIService {

    GeminiResponse processPrompt(String prompt);

    String generateLocationDescription(List<String> locationsName);

    String generateLocationDescription(String locationName);
}
