package co.com.pragma.consumer;

import co.com.pragma.model.cliente.gateway.ClienteGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements ClienteGateway {
    private final WebClient client;

    @CircuitBreaker(name = "usuarioExist")
    @Override
    public Mono<Boolean> usuarioExist(String document) {
        return client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/usuarios/exist")
                        .queryParam("document", document)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
