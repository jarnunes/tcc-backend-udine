package com.pucminas.integrations.google.vertex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucminas.Message;
import com.pucminas.integrations.google.vertex.dto.GeminiCandidate;
import com.pucminas.integrations.google.vertex.dto.GeminiPart;
import com.pucminas.integrations.google.vertex.dto.GeminiRequest;
import com.pucminas.integrations.google.vertex.dto.GeminiResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@CommonsLog
public class VertexAIServiceImpl implements VertexAIService {

    private VertexProperties vertexProperties;
    private Message message;

    @Autowired
    public void setVertexProperties(VertexProperties properties) {
        this.vertexProperties = properties;
    }

    @Autowired
    public void setMessage(Message messageIn) {
        this.message = messageIn;
    }

    @Override
    public GeminiResponse processPrompt(final String prompt) {
        log.info("Prompt: " + prompt);

        final GeminiRequest geminiRequest = GeminiRequest.GeminiRequestBuilder.prompt(prompt).build();

        ObjectMapper objectMapper = new ObjectMapper();

        // Converte o objeto para uma string JSON
        try {
            String jsonString = objectMapper.writeValueAsString(geminiRequest);
            log.info("JSON: " + jsonString);
        }catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }

        WebClient cliente =  WebClient.builder().baseUrl(vertexProperties.getGeminiUrl()).build();
        log.info("URL: " + vertexProperties.getGeminiUrl());

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
        final String prompt = message.get("vertex-ai.gemini.location.description.prompt", locationName);
        final GeminiResponse response = processPrompt(prompt);
        return processGeminiResponse(response);
    }

    @Override
    public String generateLocationDescription(List<String> locationsName) {
        final String prompt = message.get("vertex-ai.gemini.location.list.description.prompt", String.join(", ", locationsName));
        final GeminiResponse response = processPrompt(prompt);
        return processGeminiResponse(response);
    }

    private String processGeminiResponse(final GeminiResponse response) {
        return response.getCandidates().stream().findFirst().map(GeminiCandidate::getContent)
            .map(it -> it.getParts().stream().iterator().next()).map(GeminiPart::getText)
            .orElse(null);
    }

}
