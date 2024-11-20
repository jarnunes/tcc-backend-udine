package com.pucminas.integrations.openai;

import com.pucminas.integrations.LlmService;

public interface OpenAiService extends LlmService {

    String processPrompt(String prompt);

}
