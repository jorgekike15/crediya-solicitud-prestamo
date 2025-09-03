package co.com.pragma.model.cliente.gateway;

import co.com.pragma.model.cliente.Cliente;
import co.com.pragma.model.cliente.UserDocumentValidationResponse;
import reactor.core.publisher.Mono;

public interface ClienteGateway {
    Mono<UserDocumentValidationResponse> usuarioExist(String document, String token);
    Mono<Cliente> consultClienteByDocument(String document, String token);
}