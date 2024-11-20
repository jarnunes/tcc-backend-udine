package com.pucminas.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ListUtils {

    private ListUtils() {
    }

    public static <T, R> List<R> valueOrEmpty(T object, Function<T, List<R>> mapper) {
        return Optional.ofNullable(object).map(mapper).orElse(new ArrayList<>());
    }
}
