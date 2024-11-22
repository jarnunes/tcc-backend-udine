package com.pucminas.commons.functions;

import java.io.IOException;

@FunctionalInterface
public interface ResourceConsumerThrows<T> {

    /**
     * Consumer com throws explicito para carregar recursos de diretórios
     * @param resource recurso a ser carregado
     * @throws IOException caso ocorra algum erro durante a leitura do recurso
     */
    void accept(T resource) throws Throwable;

}
