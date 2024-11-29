package com.pucminas.integrations.openai;

import com.pucminas.integrations.LlmService;
import com.pucminas.integrations.google.places.dto.QuestionDefinition;

public interface OpenAiService extends LlmService {

    String processPrompt(String prompt);

    QuestionDefinition createQuestionDefinition(String question);
}
