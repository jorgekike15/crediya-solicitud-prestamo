package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SolicitudRepository {

    Mono<Solicitud> saveSolicitud(Solicitud solicitud);

    Mono<Solicitud> findById(Integer idSolicitud);

    Flux<Solicitud> findAllSolicitudes();

    Flux<Solicitud> findByIdEstadoInPaged(List<Integer> estados, int size, int offset);

    Mono<Integer> updateEstadoSolicitud(Integer idEstado, Integer idSolicitud);

    Flux<Solicitud> findAllSolicitudesByDocument(String document);
}
