package com.pucminas.commons.functions;

@FunctionalInterface
public interface ActionThrows <R> {

    R execute() throws Throwable;
}
