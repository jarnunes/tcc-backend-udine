package com.pucminas.commons.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtils {

    private ListUtils() {
    }

    public static <T> List<T> valueOrEmpty(List<T> list) {
        return CollectionUtils.isEmpty(list) ? List.of() : list;
    }

    public static <T, R> List<R> valueOrDefault(T object, Function<T, List<R>> mapper, List<R> defaultValue) {
        return Optional.ofNullable(object).map(mapper).orElse(defaultValue);
    }

    public static <T extends Comparable<T>> boolean noneMatch(List<T> values, T reference) {
        return values.stream().noneMatch(value -> value.equals(reference));
    }

    public static <T extends Comparable<T>> boolean noneMatch(List<T> values, List<T> reference) {
        return values.stream().noneMatch(reference::contains);
    }

    public static <K, V> Map<K, V> toMap(List<V> values, Function<V, K> keyMapper) {
        return values.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }
}
