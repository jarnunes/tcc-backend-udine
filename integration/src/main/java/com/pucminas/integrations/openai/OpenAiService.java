package com.pucminas.integrations.openai;

import java.util.List;

public interface OpenAiService {

    String processPrompt(String prompt);

    String generateText(String prompt);

    String generateLocationDescription(List<String> locationsName);
}
