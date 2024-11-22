package com.pucminas.integrations;

import com.pucminas.commons.utils.MessageUtils;

public class IntegrationException extends RuntimeException {

    public IntegrationException(String messageKey, Object... args) {
        super(MessageUtils.get(messageKey, args));
    }
}
