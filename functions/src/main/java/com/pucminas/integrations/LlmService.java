package com.pucminas.integrations;

import java.util.List;

public interface LlmService {

    String generateShortDescription(List<String> locationsName);

    String generateShortDescription(String locationName);

    String answerQuestion(String questionPrompt);
}
