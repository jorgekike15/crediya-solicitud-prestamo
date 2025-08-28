package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.r2dbc.entity.SolicitudEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class SolicitudReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        SolicitudEntity,
        Integer,
        SolicitudReactiveRepository
        > implements SolicitudRepository {
    public SolicitudReactiveRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
    }

    @Override
    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        SolicitudEntity entity = mapper.map(solicitud, SolicitudEntity.class);
        entity.setFechaSolicitud(java.time.LocalDate.parse(solicitud.getFechaSolicitud()));
        return repository.save(entity)
                .map(e -> mapper.map(e, Solicitud.class));
    }

    @Override
    public Flux<Solicitud> findAllSolicitudes() {
        return repository.findAll().map(entity -> mapper.map(entity,
                Solicitud.class));
    }

    @Override
    public Flux<Solicitud> findSolicitudByEstadoIn(List<Integer> estados) {
        return repository.findByIdEstadoIn(estados)
                .map(entity -> {
                    Solicitud dto = new Solicitud();
                    dto.setMonto(String.valueOf(entity.getMonto()));
                    dto.setPlazo(String.valueOf(entity.getPlazo()));
                    dto.setEmail(entity.getEmail());
                    dto.setIdEstado(entity.getIdEstado());
                    dto.setFechaSolicitud(
                            entity.getFechaSolicitud() != null ? entity.getFechaSolicitud().toString() : null
                    );
                    dto.setIdTipoPrestamo(String.valueOf(entity.getIdTipoPrestamo()));
                    dto.setDocumentoIdentificacion(entity.getDocumentoIdentificacion());
                    return dto;
                });
    }
}
