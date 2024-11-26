package com.pucminas.integrations.google.tech_to_speech;

import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechResponse;

public interface TextToSpeechService {

    TextToSpeechResponse synthesizeText(String text);
    String synthesizeTextString(String text);
}
