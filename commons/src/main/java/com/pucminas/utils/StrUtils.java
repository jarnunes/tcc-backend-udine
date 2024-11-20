package com.pucminas.utils;

import java.util.List;

public class StrUtils {

    private StrUtils() {
    }

    public static String joinComma(List<String> values) {
        return String.join(", ", values);
    }

    public static String removeMarkdownFormatting(String value) {
        return value.replace("\\*+", "");
    }

}
