package com.pucminas.commons.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ListUtils {

    private ListUtils() {
    }

    public static <T, R> List<R> valueOrDefault(T object, Function<T, List<R>> mapper, List<R> defaultValue) {
        return Optional.ofNullable(object).map(mapper).orElse(defaultValue);
    }

    public static <T extends Comparable<T>> boolean noneMatch(List<T> values, T reference) {
        return values.stream().noneMatch(value -> value.equals(reference));
    }
}
