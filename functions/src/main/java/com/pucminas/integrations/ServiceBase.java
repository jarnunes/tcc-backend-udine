package com.pucminas.integrations;

import com.pucminas.commons.functions.ActionThrows;
import com.pucminas.commons.utils.JsonUtils;
import com.pucminas.commons.utils.MessageUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.ConnectException;
import java.net.UnknownHostException;

@CommonsLog
public abstract class ServiceBase {

    protected CacheService cacheService;

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    protected abstract String serviceNameKey();

    protected String serviceName() {
        return MessageUtils.get(serviceNameKey());
    }

    protected <T, R> R processWithAttempts(final Integer connectionsAttempts, final T request, final ActionThrows<R> processRequest) {
        log.debug(MessageUtils.get("integrations.request",  JsonUtils.toJsonString(request)));

        int connectionsAttempted = 0;
        do {
            try {
                return processRequest.execute();
            } catch (Throwable e) {

                // tratar os tipos específicos de exceptions.
                log.error(MessageUtils.get("integrations.error.consumer.service", serviceName(),
                    connectionsAttempted, connectionsAttempts, e.getMessage()));

                if (connectionsAttempted == connectionsAttempts) {
                    throw new IntegrationException("integrations.error.consumer.service", serviceName(),
                        connectionsAttempted, connectionsAttempts, e.getMessage(), e);
                }

                connectionsAttempted++;
            }
        } while (connectionsAttempted < connectionsAttempts);

//        throw new IntegrationException("integrations.error.consumer.service.exit", serviceName(),
//            connectionsAttempted, connectionsAttempts);

        return null;
    }

    private boolean isConnectException(Throwable e){
        return e instanceof ConnectException || e instanceof UnknownHostException;
    }

}

