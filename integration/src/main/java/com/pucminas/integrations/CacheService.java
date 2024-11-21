package com.pucminas.integrations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class CacheService {

    private final Map<String, Cache> globalCache = Collections.synchronizedMap(new HashMap<>());

    @SuppressWarnings("unchecked")
    public <T, R> R getCacheValueOrNew(Class<?> ownerType, String key, T params, Function<T, R> getValue) {
        final String cacheKey = getCacheKey(ownerType, key, params);
        final Cache cache = globalCache.get(cacheKey);
        if (cache != null) {
            if (cache.getExpiration().isBefore(LocalDateTime.now())) {
                globalCache.remove(getCacheKey(ownerType, key, params));
            } else {
                return (R) cache.getValue();
            }
        }
        final R value = getValue.apply(params);
        globalCache.put(cacheKey, new Cache(value, LocalDateTime.now().plusDays(20)));
        return value;
    }

    private String getCacheKey(Class<?> clazz, String key, Object params) {
        return clazz.getCanonicalName() + "|" + "|" + key + ( params == null ? "null" : params.toString());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cache {
        private Object value;
        private LocalDateTime expiration;
    }
}
