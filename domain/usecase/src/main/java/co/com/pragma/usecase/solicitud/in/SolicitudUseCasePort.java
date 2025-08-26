package co.com.pragma.usecase.solicitud.in;

import co.com.pragma.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudUseCasePort {

    Mono<Solicitud> crearSolicitud(Solicitud solicitud);
    Flux<Solicitud> findAllSolicitudes();
}
