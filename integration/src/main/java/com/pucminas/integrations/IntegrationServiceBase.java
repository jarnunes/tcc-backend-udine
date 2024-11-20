package com.pucminas.integrations;

import com.pucminas.integrations.google.vertex.GeminiException;
import com.pucminas.utils.JsonUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@CommonsLog
public abstract class IntegrationServiceBase {

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

