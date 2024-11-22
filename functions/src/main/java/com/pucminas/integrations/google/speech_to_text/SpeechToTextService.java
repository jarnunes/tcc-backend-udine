package com.pucminas.integrations.google.speech_to_text;

import com.pucminas.integrations.google.speech_to_text.dto.SpeechToTextRequest;

public interface SpeechToTextService {

    String recognizeAudio(SpeechToTextRequest request);

    String recognizeAudioOGG(String audio);

    String recognizeAudioMP3(String audio);
}
