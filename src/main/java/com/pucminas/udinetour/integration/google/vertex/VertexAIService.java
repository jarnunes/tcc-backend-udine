package com.pucminas.udinetour.integration.google.vertex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pucminas.udinetour.integration.google.vertex.dto.GeminiRequest;
import com.pucminas.udinetour.integration.google.vertex.dto.GeminiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VertexAIService {

    private final String projectId = "seu-projeto-id";
    private final String location = "us-central1"; // Escolha a região onde o Vertex AI está configurado
    private final String endpointId = "seu-endpoint-id"; // ID do endpoint do modelo no Vertex AI

    private VertexProperties vertexProperties;

    @Autowired
    public void setVertexProperties(VertexProperties properties) {
        this.vertexProperties = properties;
    }


    private final RestTemplate restTemplate = new RestTemplate();

    public String processPrompt(String prompt) {
        GeminiRequest geminiRequest = GeminiRequest.GeminiRequestBuilder.prompt(prompt).build();

        // Configura os cabeçalhos
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(geminiRequest);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(vertexProperties.getGeminiUrl(),
                    HttpMethod.POST, request, String.class);

            // Processa a resposta (opcional: parse do JSON)
            GeminiResponse geminiResponse = objectMapper.readValue(response.getBody(), GeminiResponse.class);
            return geminiResponse.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
