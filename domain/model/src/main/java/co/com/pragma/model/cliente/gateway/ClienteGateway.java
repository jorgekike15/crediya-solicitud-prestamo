package co.com.pragma.model.cliente.gateway;

import reactor.core.publisher.Mono;

public interface ClienteGateway {
    Mono<Boolean> usuarioExist(String document);
}