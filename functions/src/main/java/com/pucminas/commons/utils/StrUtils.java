package com.pucminas.commons.utils;

import com.ibm.icu.text.Transliterator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class StrUtils {

    private StrUtils() {
    }

    public static String joinComma(List<String> values) {
        return String.join(", ", values);
    }

    public static String joinObjects(final String delimiter, Object... values) {
        return String.join(delimiter, Stream.of(values).filter(Objects::nonNull).map(String::valueOf).toList());
    }

    public static String joinObjects(Object... values) {
        return joinObjects(", ", values);
    }

    public static String removeMarkdownFormatting(String value) {
        return value.replaceAll("[\\n\\r\\t]+", " ") // Remove quebras de linha e tabulação
                .replaceAll("[*+_~`]", "") // Remove caracteres de formatação Markdown
                .trim();
    }


    public static String removeNonTextNumbers(String value) {
        return value.replaceAll("[^\\p{L}\\p{N}\\s]", "");
    }

    public static String normalizeAccents(String value){
        Transliterator transliterator = Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove; NFC");
        return transliterator.transliterate(value);
    }

    public static String removeDoubleQuotes(String value) {
        return value.replaceAll("\"", "");
    }

}
