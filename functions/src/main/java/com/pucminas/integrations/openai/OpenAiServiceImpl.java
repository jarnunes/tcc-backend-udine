package com.pucminas.integrations.openai;

import com.pucminas.commons.utils.JsonUtils;
import com.pucminas.commons.utils.MessageUtils;
import com.pucminas.commons.utils.StrUtils;
import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.openai.vo.OpenAiRequest;
import com.pucminas.integrations.openai.vo.OpenAiResponse;
import com.pucminas.integrations.wikipedia.dto.SearchLike;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component
@CommonsLog
public class OpenAiServiceImpl extends ServiceBase implements OpenAiService {
    private static final Semaphore rateLimiter = new Semaphore(10);
    private OpenAiProperties properties;

    @Autowired
    public void setProperties(OpenAiProperties properties) {
        this.properties = properties;
    }

    @Override
    protected String serviceNameKey() {
        return "openai.service.name";
    }

    @Override
    public String processPrompt(String prompt) {
        final OpenAiRequest request = new OpenAiRequest(prompt);

        return processWithAttempts(3, request,  () ->
                WebClient.builder().baseUrl(properties.getUrl())
                    .defaultHeader("Authorization", "Bearer " + properties.getApiKey())
                    .build()
                    .post()
                    .uri(properties.getPath())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(OpenAiResponse.class)
                    .map(it -> it.choices().iterator().next().message().content())
                    .block());
    }


    @Override
    public String generateShortDescription(List<String> locationsName) {
        final String prompt = MessageUtils.get("openai.generate.locations.short.description.prompt", StrUtils.joinComma(locationsName));
        return processPromptWithRateLimiting(prompt);
    }

    @Override
    public String generateShortDescription(String locationName) {
        final String prompt = MessageUtils.get("openai.generate.location.short.description.prompt", locationName);
        return processPromptWithRateLimiting(prompt);
    }

    @Override
    public String answerQuestion(String questionPrompt) {
        return processPromptWithRateLimiting(questionPrompt);
    }

    @Override
    public String findCorrectPlaceTitle(String searchTitle, List<SearchLike> searchResults) {
        final String prompt = MessageUtils.get("openai.find.location.name", searchTitle, JsonUtils.toJsonString(searchResults));
        final String response = processPromptWithRateLimiting(prompt);
        return StringUtils.equals("NOT_FOUND", response) ? null : StringUtils.replace(response, "\"", "");
    }

    private String processPromptWithRateLimiting(String prompt) {
        try {
            if (rateLimiter.tryAcquire(30, TimeUnit.SECONDS)) { // Aguarda até 30 segundos
                return processPrompt(prompt);
            } else {
                throw new RuntimeException("Rate limit exceeded locally");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for rate limiter", e);
        } finally {
            rateLimiter.release();
        }
    }

}
