package co.com.pragma.usecase.solicitud.in;

import co.com.pragma.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudUseCasePort {

    Mono<Solicitud> crearSolicitud(Solicitud solicitud, String token);
    Flux<Solicitud> findAllSolicitudes();
    Flux<Solicitud> findSolicitudPendienteRechazadaRevision(Integer page, Integer size);
}
