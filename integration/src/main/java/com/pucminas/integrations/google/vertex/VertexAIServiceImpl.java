package com.pucminas.integrations.google.vertex;

import com.pucminas.integrations.IntegrationServiceBase;
import com.pucminas.integrations.google.vertex.dto.GeminiCandidate;
import com.pucminas.integrations.google.vertex.dto.GeminiPart;
import com.pucminas.integrations.google.vertex.dto.GeminiRequest;
import com.pucminas.integrations.google.vertex.dto.GeminiResponse;
import com.pucminas.utils.MessageUtils;
import com.pucminas.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class VertexAIServiceImpl extends IntegrationServiceBase implements VertexAIService {

    private VertexProperties vertexProperties;

    @Autowired
    public void setVertexProperties(VertexProperties properties) {
        this.vertexProperties = properties;
    }

    @Override
    public GeminiResponse processPrompt(final String prompt) {
        final GeminiRequest geminiRequest = GeminiRequest.GeminiRequestBuilder.prompt(prompt).build();


        return proccess("Serviço Google VertexAI", geminiRequest, vertexProperties.getConnectionsAttempt(),
                vertexProperties.getGeminiUrl(), GeminiResponse.class, clientBuilder -> {
                }, WebClient::post,
                mono -> mono);
    }

    @Override
    public String generateShortDescription(List<String> locationsName) {
        final String prompt = MessageUtils.get("vertex-ai.gemini.generate.locations.short.description.prompt", StrUtils.joinComma(locationsName));
        final GeminiResponse response = processPrompt(prompt);
        return processGeminiResponse(response);
    }

    @Override
    public String generateShortDescription(String locationName) {
        final String prompt = MessageUtils.get("vertex-ai.gemini.generate.location.short.description.prompt", locationName);
        final GeminiResponse response = processPrompt(prompt);
        return processGeminiResponse(response);
    }

    @Override
    public String answerQuestion(String questionPrompt) {
        final GeminiResponse response = processPrompt(questionPrompt);
        return processGeminiResponse(response);
    }

    private String processGeminiResponse(final GeminiResponse response) {
        return response.getCandidates().stream().findFirst()
                .map(GeminiCandidate::getContent)
                .map(it -> it.getParts().stream().iterator().next())
                .map(GeminiPart::getText)
                .map(StrUtils::removeMarkdownFormatting)
                .orElse("Nenhum texto retornado pelo Gemini.");
    }

}
