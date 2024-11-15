package com.pucminas.integrations.google.speech_to_text;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.ApiKeyCredentials;
import com.google.cloud.speech.v1p1beta1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
// https://cloudconvert.com/ogg-to-wav

@Service
public class SpeechToTextServiceImpl implements SpeechToTextService {

    private STTProperties properties;

    @Autowired
    private void setProperties(STTProperties properties) {
        this.properties = properties;
    }

    @Override
    public String speechToText(String audio) throws IOException {
        final ApiKeyCredentials credentials = ApiKeyCredentials.create(properties.getApiKey());
        final SpeechSettings speechSettings = SpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        final byte[] bytes = Base64.getDecoder().decode(audio);

        try (SpeechClient speechClient = SpeechClient.create(speechSettings)) {
            final ByteString audioBytes = ByteString.copyFrom(bytes);
            final RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.OGG_OPUS)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("pt-BR")
                    .setEnableWordTimeOffsets(false)
                    .build();

            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);

            final StringBuilder transcription = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResultsList()) {
                transcription.append(result.getAlternativesList().get(0).getTranscript());
            }
            return transcription.toString();
        }
    }

}
