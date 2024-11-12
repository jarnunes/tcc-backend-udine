package com.pucminas.integrations.google.speech_to_text;

import java.io.IOException;
import java.io.InputStream;

public interface SpeechToTextService {


    String speechToText(InputStream audio) throws IOException;

}
