package com.pucminas.integrations.google.speech_to_text;

import com.google.cloud.speech.v1p1beta1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class SpeechToTextServiceImpl implements SpeechToTextService {

    @Override
    public String speechToText(InputStream audioFile) throws IOException {
        try (SpeechClient speechClient = SpeechClient.create()) {
            final ByteString audioBytes = ByteString.readFrom(audioFile);

            // Configurações para reconhecimento de fala
            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16) // Formato de áudio
                    .setSampleRateHertz(16000) // Taxa de amostragem
                    .setLanguageCode("en-US") // Idioma
                    .build();

            // Configuração do áudio a ser processado
            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Realiza a transcrição
            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);

            // Retorna o texto transcrito
            StringBuilder transcription = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResultsList()) {
                transcription.append(result.getAlternativesList().get(0).getTranscript());
            }
            return transcription.toString();
        }
    }
}
