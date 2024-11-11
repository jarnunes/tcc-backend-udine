package com.pucminas.functionlayer.integrations.google.vertex;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.jnunes.spgconfig.Message;
import com.pucminas.functionlayer.integrations.google.vertex.dto.GeminiCandidate;
import com.pucminas.functionlayer.integrations.google.vertex.dto.GeminiRequest;
import com.pucminas.functionlayer.integrations.google.vertex.dto.GeminiPart;
import com.pucminas.functionlayer.integrations.google.vertex.dto.GeminiResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@CommonsLog
public class VertexAIServiceImpl implements VertexAIService {

    private final RestTemplate restTemplate = new RestTemplate();
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
    public String generateLocationDescription(String locationName) {
        final String prompt = message.get("vertex-ai.gemini.location.description.prompt", locationName);
        final GeminiResponse response = processPrompt(prompt);
        try {
            return response.getCandidates().stream().findFirst().map(GeminiCandidate::getContent)
                    .map(it -> it.getParts().stream().iterator().next()).map(GeminiPart::getText)
                    .orElse(null);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    private GeminiResponse processPrompt(final String prompt) {
        final GeminiRequest geminiRequest = GeminiRequest.GeminiRequestBuilder.prompt(prompt).build();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(geminiRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(vertexProperties.getGeminiUrl(),
                    HttpMethod.POST, request, String.class);

            return objectMapper.readValue(response.getBody(), GeminiResponse.class);
        } catch (Exception e) {
            throw new GeminiException(e);
        }
    }

}
