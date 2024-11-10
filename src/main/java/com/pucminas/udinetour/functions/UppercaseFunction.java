package com.pucminas.udinetour.functions;

import java.util.function.Function;

public class UppercaseFunction implements Function<String, String> {

    @Override
    public String apply(String s) {
        return s.toUpperCase();
    }
}
