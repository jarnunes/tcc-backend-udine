package com.pucminas.integrations.google.speech_to_text;

import java.io.IOException;
import java.io.InputStream;

// https://docs.spring.io/spring-ai/reference/api/audio/speech/openai-speech.html
public interface SpeechToTextService {


    String speechToText(InputStream audio) throws IOException;

}
