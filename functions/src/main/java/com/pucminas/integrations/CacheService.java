package com.pucminas.integrations;

import com.pucminas.commons.resource.FileResource;
import com.pucminas.commons.utils.StrUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@CommonsLog
@EnableScheduling
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

    @Scheduled(fixedRate = 3600000, initialDelay = 3600000)
    private void clearCacheScheduled() {
        log.info("Clearing cache scheduled");

        final AtomicBoolean hasRemovedAnyKey = new AtomicBoolean(false);
        globalCache.forEach((key, value) -> {
            if (value.getExpiration().isBefore(LocalDateTime.now())) {
                globalCache.remove(key);
                hasRemovedAnyKey.set(true);
            }
        });

        if (hasRemovedAnyKey.get()) {
            saveCache();
        }
    }

    public void clearAllCache() {
        globalCache.clear();
        saveCache();
    }

    public void putImageFile(String fileName, byte[] value){
        putCache(fileName, "Save in localFile");
        FileResource.instance().writerImage(fileName, value);
    }

    public byte[] getImageFile(String fileName, Supplier<byte[]> getFile){
        final Cache cache = globalCache.get(fileName);
        if (cache != null) {
            if (cache.getExpiration().isBefore(LocalDateTime.now())) {
                globalCache.remove(fileName);
                FileResource.instance().removeImage(fileName);
            } else {
                final byte[] value = FileResource.instance().readImage(fileName);
                if (ArrayUtils.isNotEmpty(value))
                    return value;
            }
        }

        final byte[] value = getFile.get();
        putImageFile(fileName, value);

        return value;
    }

    public <T> void putCache(Class<?> ownerType, String key, T params, Object value) {
        final String cacheKey = getCacheKey(ownerType, key, params);
        putCache(cacheKey, value);
    }

    public void removeCacheItem(Class<?> ownerType, String key, Object params) {
        final String cacheKey = getCacheKey(ownerType, key, params);
        globalCache.remove(cacheKey);
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
        putCache(cacheKey, value);

        return value;
    }

    public <T> void putCache(String key, T value) {
        globalCache.put(key, new Cache(value, LocalDateTime.now().plusDays(20)));
    }

    public <T> T getCachedValue(String key){
        final Cache cache = globalCache.get(key);
        if (cache != null) {
            if (cache.getExpiration().isBefore(LocalDateTime.now())) {
                globalCache.remove(key);
            } else {
                return (T) cache.getValue();
            }
        }
        return null;
    }

    private String getCacheKey(Class<?> clazz, String key, Object params) {
        final String paramsKey = params == null ? "null" : params.toString();
        return StrUtils.joinObjects("|", clazz.getCanonicalName(), key,  paramsKey);
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
