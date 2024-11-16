package com.pucminas.integrations.google.tech_to_speech;


import com.pucminas.Message;
import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechRequest;
import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TextToSpeechServiceImpl implements TextToSpeechService {
    private TTSProperties properties;

    @Autowired
    public void setProperties(TTSProperties properties) {
        this.properties = properties;
    }

    private Message message;

    @Autowired
    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public TextToSpeechResponse synthesizeText(String text) {
        final TextToSpeechRequest request = TextToSpeechRequest.builder().withInput(text).build();
        final TextToSpeechResponse response = new TextToSpeechResponse();
        response.setAudioContent(message.get("text-to-speech.default.response"));
        return response;
//        return WebClient.builder().baseUrl(properties.getBaseURI()).build()
//                .post().uri(uriBuilder -> uriBuilder
//                        .path(properties.getPath())
//                        .queryParam("key", properties.getApiKey())
//                        .build())
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromValue(request))
//                .retrieve()
//                .bodyToMono(TextToSpeechResponse.class)
//                .block();
    }
}
