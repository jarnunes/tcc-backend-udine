package com.pucminas.integrations.google.text_to_speech;

import com.pucminas.integrations.google.text_to_speech.dto.TextToSpeechResponse;

public interface TextToSpeechService {

    TextToSpeechResponse synthesizeText(String text);
}
