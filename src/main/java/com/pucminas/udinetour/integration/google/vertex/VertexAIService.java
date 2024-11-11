package com.pucminas.udinetour.integration.google.vertex;

import com.pucminas.udinetour.integration.google.vertex.dto.GeminiResponse;

public interface VertexAIService {

    String generateLocationDescription(String locationName);
}
