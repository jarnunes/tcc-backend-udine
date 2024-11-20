package com.pucminas.integrations.openai;

import com.pucminas.integrations.openai.vo.OpenAiRequest;
import com.pucminas.integrations.openai.vo.OpenAiResponse;
import com.pucminas.utils.JsonUtils;
import com.pucminas.utils.MessageUtils;
import com.pucminas.utils.StrUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@CommonsLog
public class OpenAiServiceImpl implements OpenAiService {
    private OpenAiProperties properties;

    @Autowired
    public void setProperties(OpenAiProperties properties) {
        this.properties = properties;
    }

    @Override
    public String processPrompt(String prompt) {
        final OpenAiRequest request = new OpenAiRequest(prompt);
        log.info("Request: " + JsonUtils.toJsonString(request));

        try {
            return WebClient.builder().baseUrl("https://api.openai.com")
                    .defaultHeader("Authorization", "Bearer " + properties.getApiKey())
                    .build()
                    .post()
                    .uri("/v1/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(OpenAiResponse.class)
                    .map(it -> it.choices().iterator().next().message().content())
                    .block();
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String generateShortDescription(List<String> locationsName) {
        final String prompt = MessageUtils.get("openai.generate.locations.short.description.prompt", StrUtils.joinComma(locationsName));
        return processPrompt(prompt);
    }

    @Override
    public String generateShortDescription(String locationName) {
        final String prompt = MessageUtils.get("openai.generate.location.short.description.prompt", locationName);
        return processPrompt(prompt);
    }

    @Override
    public String answerQuestion(String questionPrompt) {
        return processPrompt(questionPrompt);
    }
}
