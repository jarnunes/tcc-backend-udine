package com.pucminas.api;

import com.pucminas.utils.JsonUtils;
import lombok.extern.apachecommons.CommonsLog;

import java.util.function.Function;

@CommonsLog
public abstract class FunctionBase {

    protected abstract String serviceName();

    protected <T, R> R processRequest(T request, Function<T, R> service) {
        logRequest(request);
        final R response = service.apply(request);
        logResponse(response);
        return response;
    }

    protected void logRequest(Object request) {
        log.info(String.format("Request to %s: ", serviceName()) + JsonUtils.toJsonString(request));
    }

    protected void logResponse(Object response) {
        log.info(String.format("Response to %s: ", serviceName()) + JsonUtils.toJsonString(response));
    }
}
