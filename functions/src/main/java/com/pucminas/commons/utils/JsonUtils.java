package com.pucminas.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pucminas.commons.JsonException;

public class JsonUtils {

    private JsonUtils() {
    }

    public static <T> String toJsonString(T object) {
        ObjectMapper objectMapper = getObjectMapperInstance();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonException(MessageUtils.get("json.err.convert.object.to.string"), e);
        }
    }

    public static <T> T toObject(String jsonString, Class<T> clazz) {
        ObjectMapper objectMapper = getObjectMapperInstance();
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonException(MessageUtils.get("json.err.convert.string.to.object", clazz.getCanonicalName()), e);
        }
    }

    private static ObjectMapper getObjectMapperInstance() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

}
