package com.pucminas.udinetour.functions;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ConcatFunction implements Function<String, String> {

    @Override
    public String apply(String s) {
        return "Nova String " + s;
    }
}
