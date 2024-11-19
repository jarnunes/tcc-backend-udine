package com.pucminas.integrations.openai;

import com.pucminas.integrations.openai.vo.OpenAiRequest;
import com.pucminas.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class OpenAiServiceImpl implements OpenAiService {
    private OpenAiProperties properties;

    @Autowired
    public void setProperties(OpenAiProperties properties) {
        this.properties = properties;
    }

    @Override
    public String processPrompt(String prompt) {
        return WebClient.builder().baseUrl(properties.getUrl()).build()
                .post()
                .header("Authorization", "Bearer " + properties.getApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(BodyInserters.fromValue(new OpenAiRequest(prompt)))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public String generateText(String text) {
        final String prompt = MessageUtils.get("openai.questions.locations.prompt", text);
        return processPrompt(prompt);
    }

    @Override
    public String generateLocationDescription(List<String> locationsName) {
        final String prompt = MessageUtils.get("openai.gemini.location.list.description.prompt", String.join(", ", locationsName));
        return processPrompt(prompt);
    }
}
