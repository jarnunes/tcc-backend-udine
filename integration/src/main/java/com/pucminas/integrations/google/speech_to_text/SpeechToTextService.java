package com.pucminas.integrations.google.speech_to_text;

// https://docs.spring.io/spring-ai/reference/api/audio/speech/openai-speech.html
public interface SpeechToTextService {

    String recognize(String audio);
}
