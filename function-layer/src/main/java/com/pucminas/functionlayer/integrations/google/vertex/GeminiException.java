package com.pucminas.functionlayer.integrations.google.vertex;

public class GeminiException extends RuntimeException {

    public GeminiException(String message) {
        super(message);
    }

    public GeminiException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeminiException(Throwable cause) {
        super(cause);
    }
}
