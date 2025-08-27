package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.cliente.gateway.ClienteGateway;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.usecase.solicitud.in.SolicitudUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase implements SolicitudUseCasePort {

    private final SolicitudRepository solicitudRepository;
    private final ClienteGateway clienteGateway;

    @Override
    public Mono<Solicitud> crearSolicitud(Solicitud solicitud, String token) {
        return clienteGateway.usuarioExist(solicitud.getDocumentoIdentificacion(), token)
                .flatMap(userValidation -> {
                    if (userValidation.isExist()) {
                        return solicitudRepository.saveSolicitud(solicitud);
                    }
                    return Mono.error(new IllegalArgumentException(userValidation.getMessage()));
                });
    }

    @Override
    public Flux<Solicitud> findAllSolicitudes(){
        return solicitudRepository.findAllSolicitudes();
    }

}
