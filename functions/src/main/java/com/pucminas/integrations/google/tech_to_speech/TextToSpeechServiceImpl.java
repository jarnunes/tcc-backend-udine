package com.pucminas.integrations.google.tech_to_speech;


import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechRequest;
import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TextToSpeechServiceImpl extends ServiceBase implements TextToSpeechService {
    private TTSProperties properties;

    @Autowired
    public void setProperties(TTSProperties properties) {
        this.properties = properties;
    }

    @Override
    protected String serviceNameKey() {
        return "texttospeech.service.name";
    }

    @Override
    public TextToSpeechResponse synthesizeText(String text) {
        return cacheService.getCachedValueOrNew(getClass(), "CACHE_KEY_SYNTHESIZE_TEXT", text,
            this::synthesize);
    }

    public TextToSpeechResponse synthesize(String text) {
        return processWithAttempts(3, text, () -> {
            final TextToSpeechRequest request = TextToSpeechRequest.builder().withInput(text).build();
            return WebClient.builder()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024))
                    .baseUrl(properties.getBaseURI()).build()
                    .post().uri(uriBuilder -> uriBuilder
                            .path(properties.getPath())
                            .queryParam("key", properties.getApiKey())
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .bodyToMono(TextToSpeechResponse.class)
                    .block();
        });
    }
}
