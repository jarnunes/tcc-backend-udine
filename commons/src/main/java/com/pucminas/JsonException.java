package com.pucminas;


import com.pucminas.utils.MessageUtils;

import java.io.Serial;

public class JsonException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3483626010777962318L;

    public JsonException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }

    public static JsonException forToStringConverter(Throwable rootCause) {
        return new JsonException(MessageUtils.get("json.err.convert.object.to.string"), rootCause);
    }
}
