package com.pucminas.udinetour.functions;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UppercaseFunction implements Function<String, String> {

    @Override
    public String apply(String s) {
        return s.toUpperCase();
    }
}
