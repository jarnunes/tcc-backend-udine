package com.pucminas.integrations.openai.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiRequest {
    String model = "text-davinci-003";

    String prompt;

    @JsonProperty("max_tokens")
    int maxTokens = 150;

    public OpenAiRequest(String prompt) {
        this.prompt = prompt;
    }
}
