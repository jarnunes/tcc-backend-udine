package com.pucminas.commons.utils;

import java.util.List;
import java.util.stream.Stream;

public class StrUtils {

    private StrUtils() {
    }

    public static String joinComma(List<String> values) {
        return String.join(",", values);
    }

    public static String joinObjects(Object... values) {
        return String.join(",", Stream.of(values).map(Object::toString).toList());
    }

    public static String removeMarkdownFormatting(String value) {
        return value.replace("\\*+", "");
    }

}
