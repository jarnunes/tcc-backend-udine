package com.pucminas;

import com.pucminas.utils.MessageUtils;

import java.io.Serial;

public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -977033582391086078L;

    public ValidationException(String messageKey) {
        super(MessageUtils.get(messageKey));
    }

    public ValidationException(String msgKey, Object... args) {
        super(MessageUtils.get(msgKey, args));
    }

}