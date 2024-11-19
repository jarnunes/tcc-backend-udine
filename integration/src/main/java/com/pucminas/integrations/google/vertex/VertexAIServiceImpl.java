package com.pucminas.integrations.google.vertex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucminas.integrations.google.vertex.dto.GeminiCandidate;
import com.pucminas.integrations.google.vertex.dto.GeminiPart;
import com.pucminas.integrations.google.vertex.dto.GeminiRequest;
import com.pucminas.integrations.google.vertex.dto.GeminiResponse;
import com.pucminas.utils.MessageUtils;
import com.pucminas.utils.StrUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@CommonsLog
public class VertexAIServiceImpl implements VertexAIService {

    private VertexProperties vertexProperties;

    @Autowired
    public void setVertexProperties(VertexProperties properties) {
        this.vertexProperties = properties;
    }

    @Override
    public GeminiResponse processPrompt(final String prompt) {
        log.info("Prompt: " + prompt);

        final GeminiRequest geminiRequest = GeminiRequest.GeminiRequestBuilder.prompt(prompt).build();

        ObjectMapper objectMapper = new ObjectMapper();

        // Converte o objeto para uma string JSON
        try {
            String jsonString = objectMapper.writeValueAsString(geminiRequest);
            log.debug("JSON: " + jsonString);
        }catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }

        WebClient cliente =  WebClient.builder().baseUrl(vertexProperties.getGeminiUrl()).build();
        log.debug("URL: " + vertexProperties.getGeminiUrl());

        return cliente
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(geminiRequest))
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block();
    }

    @Override
    public String generateLocationDescription(String locationName) {
        final String prompt = MessageUtils.get("vertex-ai.gemini.generate.location.short.description.prompt", locationName);
        final GeminiResponse response = processPrompt(prompt);
        return processGeminiResponse(response);
    }

    @Override
    public String generateLocationDescription(List<String> locationsName) {
        final String prompt = MessageUtils.get("vertex-ai.gemini.generate.locations.short.description.prompt", StrUtils.joinComma(locationsName));
        final GeminiResponse response = processPrompt(prompt);
        return processGeminiResponse(response);
    }

    @Override
    public String answerQuestion(String questionPrompt) {
        final GeminiResponse response = processPrompt(questionPrompt);
        return processGeminiResponse(response);
    }

    private String processGeminiResponse(final GeminiResponse response) {
        return response.getCandidates().stream().findFirst().map(GeminiCandidate::getContent)
            .map(it -> it.getParts().stream().iterator().next()).map(GeminiPart::getText)
            .orElse(null);
    }

}
