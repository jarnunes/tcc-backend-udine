package com.pucminas.integrations;

import com.pucminas.commons.functions.ActionThrows;
import com.pucminas.commons.utils.JsonUtils;
import com.pucminas.commons.utils.MessageUtils;
import com.pucminas.integrations.google.vertex.GeminiException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

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

    protected <T, R> R proccess(final String serviceName, final T request,
                                final Integer connectionsAttempt, final String baseURL, final Class<R> clazzType,
                                final Consumer<WebClient.Builder> clientBuilderConsumer,
                                final Function<WebClient, WebClient.RequestBodyUriSpec> webClientProcess,
                                final UnaryOperator<Mono<R>> responseMapper) {

        int connectionsAttempted = 0;

        log.info("REQUEST: " + JsonUtils.toJsonString(request));
        final WebClient.Builder clientBuilder = WebClient.builder().baseUrl(baseURL);

        do {
            try {
                clientBuilderConsumer.accept(clientBuilder);

                final WebClient webClient = clientBuilder.build();
                final WebClient.RequestBodyUriSpec uriSpec = webClientProcess.apply(webClient);
                uriSpec.contentType(MediaType.APPLICATION_JSON);
                uriSpec.body(BodyInserters.fromValue(request));

                final Mono<R> mono = responseMapper.apply(uriSpec.retrieve().bodyToMono(clazzType));

                return mono.block();

            } catch (Exception e) {
                final String msg = "Falha ao processar requisição para o serviço " + serviceName + ". Tentativa " + connectionsAttempted + " de " + connectionsAttempt;
                log.error(msg, e);

                if (connectionsAttempted == connectionsAttempt) {
                    throw new GeminiException(msg, e);
                }

                connectionsAttempted++;
            }
        } while (connectionsAttempted < connectionsAttempt);

        throw new GeminiException("Falha ao processar requisição para o serviço " + serviceName);
    }
}

