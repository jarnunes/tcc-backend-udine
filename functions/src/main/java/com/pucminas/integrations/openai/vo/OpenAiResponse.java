package com.pucminas.integrations.openai.vo;

import java.util.List;

public record OpenAiResponse(List<Choice> choices) {
}
