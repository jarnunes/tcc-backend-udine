package com.pucminas.commons.functions;

@FunctionalInterface
public interface FunctionThrows<T, R> {

    R apply(T value) throws Throwable;
}
