package com.pucminas.integrations;

import com.pucminas.commons.resource.FileResource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@CommonsLog
public class CacheService {
    private static final String CACHE_FILE = "cache.ser";
    private Map<String, Cache> globalCache ;

    @PostConstruct
    public void init() {
        loadCache();
    }

    @PreDestroy
    public void destroy() {
        saveCache();
    }

    @SuppressWarnings("unchecked")
    public <T, R> R getCachedValueOrNew(Class<?> ownerType, String key, T params, Function<T, R> getValue) {
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

    private void saveCache() {
        FileResource.instance().writer(CACHE_FILE, globalCache);
    }

    @SuppressWarnings("unchecked")
    private void loadCache() {
        FileResource.instance().loadFile(CACHE_FILE,
                inputStream -> globalCache = ObjectUtils.defaultIfNull((Map<String, Cache>) inputStream.readObject(), new HashMap<>()),
                file -> setNewCache(),
                exception -> setNewCache());
    }

    private void setNewCache(){
        globalCache = new HashMap<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cache implements Serializable {

        @Serial
        private static final long serialVersionUID = -486258422967089650L;

        private Object value;
        private LocalDateTime expiration;
    }
}
