package com.pucminas.integrations.google.speech_to_text;

import com.pucminas.integrations.google.speech_to_text.dto.Result;
import com.pucminas.integrations.google.speech_to_text.dto.SpeechToTextRequest;
import com.pucminas.integrations.google.speech_to_text.dto.SpeechToTextResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
// https://cloudconvert.com/ogg-to-wav

@Service
public class SpeechToTextServiceImpl implements SpeechToTextService {

    private STTProperties properties;

    @Autowired
    private void setProperties(STTProperties properties) {
        this.properties = properties;
    }


    public String recognize(String audio) {
        final SpeechToTextRequest request = new SpeechToTextRequest(audio);

        return WebClient.builder().baseUrl(properties.getBaseURI()).build()
                .post().uri(uriBuilder -> uriBuilder
                        .path(properties.getPath())
                        .queryParam("key", properties.getApiKey())
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(SpeechToTextResponse.class)
                .map(SpeechToTextResponse::getResults)
                .map(this::getTranscript)
                .block();
    }

    private String getTranscript(List<Result> results) {
        if (results != null && !results.isEmpty()) {
            Result result = results.get(0);
            return result.getAlternatives().get(0).getTranscript();
        }
        return "";
    }

}
