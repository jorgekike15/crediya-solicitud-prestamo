package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SolicitudRepository {

    Mono<Solicitud> saveSolicitud(Solicitud solicitud);

    Mono<Solicitud> findById(Integer idSolicitud);

    Flux<Solicitud> findAllSolicitudes();

    Flux<Solicitud> findSolicitudByEstadoIn(List<Integer> estados);

    Mono<Integer> updateSolicitud(Integer idSolicitud, Integer idEstado);
}
