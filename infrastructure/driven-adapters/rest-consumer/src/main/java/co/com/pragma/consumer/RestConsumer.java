package co.com.pragma.consumer;

import co.com.pragma.model.cliente.Cliente;
import co.com.pragma.model.cliente.UserDocumentValidationResponse;
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
    public Mono<UserDocumentValidationResponse> usuarioExist(String document, String token) {
        String authHeader = token.startsWith("Bearer ") ? token : "Bearer " + token;
        return client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/usuarios/exist")
                        .queryParam("document", document)
                        .build())
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(UserDocumentValidationResponse.class);
    }

    @Override
    public Mono<Cliente> consultClienteByDocument(String document, String token) {
        String authHeader = token.startsWith("Bearer ") ? token : "Bearer " + token;
        return client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/usuarios")
                        .queryParam("document", document)
                        .build())
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(Cliente.class);
    }
}
