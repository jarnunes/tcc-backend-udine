package com.pucminas.utils;


import com.pucminas.Message;
import com.pucminas.StaticContextAccessor;
import org.apache.commons.lang3.StringUtils;

public class MessageUtils {
    private MessageUtils() {
    }

    public static String get(String messageKey, Object... args) {
        return StaticContextAccessor.getBean(Message.class).get(messageKey, args);
    }

    public static String defaultIfEmpty(String value, String defaultMessageKey, Object... args) {
        return StringUtils.isNotBlank(value) ? value : MessageUtils.get(defaultMessageKey, args);
    }

    public static String get(String messageKey) {
        return StaticContextAccessor.getBean(Message.class).get(messageKey);
    }
}