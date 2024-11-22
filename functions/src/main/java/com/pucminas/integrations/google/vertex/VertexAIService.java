package com.pucminas.integrations.google.vertex;

import com.pucminas.integrations.LlmService;
import com.pucminas.integrations.google.vertex.dto.GeminiResponse;

public interface VertexAIService extends LlmService {

    GeminiResponse processPrompt(String prompt);

}
