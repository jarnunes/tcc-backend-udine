package com.pucminas.utils;


import com.pucminas.Message;
import com.pucminas.StaticContextAccessor;

public class MessageUtils {
    private MessageUtils() {
    }

    public static String get(String messageKey, Object... args) {
        return StaticContextAccessor.getBean(Message.class).get(messageKey, args);
    }

    public static String get(String messageKey) {
        return StaticContextAccessor.getBean(Message.class).get(messageKey);
    }
}