package com.pucminas.integrations.google.tts;

import com.pucminas.integrations.google.tts.dto.TextToSpeechResponse;

public interface TextToSpeechService {

    TextToSpeechResponse synthesizeText(String text);
}
