package com.pucminas.integrations.google.vertex;

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
        return WebClient.builder().baseUrl(vertexProperties.getGeminiUrl()).build()
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(geminiRequest))
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block();
    }

}
