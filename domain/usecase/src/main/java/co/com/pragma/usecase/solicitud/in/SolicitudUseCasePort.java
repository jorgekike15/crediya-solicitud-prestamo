package co.com.pragma.usecase.solicitud.in;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.SolicitudResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudUseCasePort {

    Mono<Solicitud> crearSolicitud(Solicitud solicitud, String token);
    Flux<Solicitud> findAllSolicitudes();
    Mono<SolicitudResponse> findSolicitudPendienteRechazadaRevision(Integer page, Integer size);
    Mono<Boolean> gestionarSolicitud(Integer idEstado, Integer idSolicitud);
    
}
