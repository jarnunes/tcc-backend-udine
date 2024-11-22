package com.pucminas.commons.resource;


import java.io.Serial;

public class ResourceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4541022146938738701L;

    public ResourceException(Throwable e) {
        super(e);
    }
}
