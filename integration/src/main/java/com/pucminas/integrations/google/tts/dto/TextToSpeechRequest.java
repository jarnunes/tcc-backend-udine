package com.pucminas.integrations.google.tts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TextToSpeechRequest {

    private AudioConfig audioConfig;
    private Input input;
    private Voice voice;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final TextToSpeechRequest request;

        private Builder() {
            request = new TextToSpeechRequest();
            withAudioConfig();
            withVoice();
        }

        private void withAudioConfig() {
            request.setAudioConfig(new AudioConfig("MP3"));
        }

        public Builder withInput(String text) {
            request.setInput(new Input(text));
            return this;
        }

        private void withVoice() {
            request.setVoice(new Voice("pt-BR", null));
        }

        public TextToSpeechRequest build() {
            return request;
        }

    }
}
