package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.cliente.gateway.ClienteGateway;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final ClienteGateway clienteGateway;

    public Mono<Solicitud> saveSolicitud(Solicitud solicitud){
        return clienteGateway.usuarioExist(solicitud.getDocumentoIdentificacion())
                .flatMap(existe -> {
                    if (!existe) {
                        return Mono.error(new IllegalArgumentException("Cliente no válido"));
                    }
                    return solicitudRepository.saveSolicitud(solicitud);
                });
    }

    public Flux<Solicitud> findAllSolicitudes(){
        return solicitudRepository.findAllSolicitudes();
    }

    public Flux<Solicitud> findByEmail(String email){
        return solicitudRepository.findByEmail(email);
    }

}
