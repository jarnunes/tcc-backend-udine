package com.pucminas.integrations.openai.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiRequest {
    private String model = "gpt-4";

    private List<MessageRequest> messages = new ArrayList<>();

    @JsonProperty("max_tokens")
    private int maxTokens = 150;
    private double temperature = 0.7;

    public OpenAiRequest(String prompt) {
        this.messages.add(new MessageRequest(prompt));
    }
}
