package com.pucminas.integrations.google.speech_to_text;

import java.io.IOException;

// https://docs.spring.io/spring-ai/reference/api/audio/speech/openai-speech.html
public interface SpeechToTextService {

    String speechToText(String audio) throws IOException;

}
