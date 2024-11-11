package com.pucminas.udinetour.commons;

import lombok.EqualsAndHashCode;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.HashMap;

@Component
@CommonsLog
@EqualsAndHashCode(callSuper = false)
public class Message extends HashMap<String, String> {

    @Serial
    private static final long serialVersionUID = -4426127768495622736L;

    @Lazy
    private transient MessageSource messageSource;

    @Autowired
    private void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String getMessage(final String key) {
        try {
            return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            log.error(String.valueOf(e));
            return key;
        }
    }

    @Override
    public String get(Object key) {
        return this.getMessage((String) key);
    }

    public String get(String key, Object... params) {
        final MessageFormat format = new MessageFormat(this.getMessage(key));
        return format.format(params);
    }
}