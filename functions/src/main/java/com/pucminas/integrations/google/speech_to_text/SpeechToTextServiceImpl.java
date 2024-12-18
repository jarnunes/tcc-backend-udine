package com.pucminas.integrations.google.speech_to_text;

import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.google.speech_to_text.dto.AudioEncodingType;
import com.pucminas.integrations.google.speech_to_text.dto.Result;
import com.pucminas.integrations.google.speech_to_text.dto.SpeechToTextRequest;
import com.pucminas.integrations.google.speech_to_text.dto.SpeechToTextResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class SpeechToTextServiceImpl extends ServiceBase implements SpeechToTextService {

    private STTProperties properties;

    @Autowired
    private void setProperties(STTProperties properties) {
        this.properties = properties;
    }

    @Override
    protected String serviceNameKey() {
        return "speechtotext.service.name";
    }

    @Override
    public String recognizeAudio(SpeechToTextRequest request) {
        return cacheService.getCachedValueOrNew(getClass(), "CACHE_KEY_RECOGNIZE_AUDIO", request,
            this::recognize);
    }

    private String recognize(SpeechToTextRequest request){
        return processWithAttempts(3, request, () -> WebClient.builder()
                .baseUrl(properties.getBaseURI()).build()
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
                .block());
    }

    @Override
    public String recognizeAudioOGG(String audio) {
        return recognizeAudio(new SpeechToTextRequest(audio));
    }

    @Override
    public String recognizeAudioMP3(String audio) {
        final SpeechToTextRequest request = new SpeechToTextRequest(audio);
        request.getConfig().setEncoding(AudioEncodingType.MP3);
        return recognizeAudio(request);
    }

    private String getTranscript(List<Result> results) {
        if (results != null && !results.isEmpty()) {
            Result result = results.get(0);
            return result.getAlternatives().get(0).getTranscript();
        }
        //TODO: Retornar uma mensagem significativa
        return "";
    }

}
