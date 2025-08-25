package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.r2dbc.entity.SolicitudEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        SolicitudEntity,
        Integer,
        MyReactiveRepository
        > implements SolicitudRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Solicitud.class/* change for domain model */));
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
    public Flux<Solicitud> findByEmail(String email) {
        return repository.findByEmail(email).map(entity -> mapper.map(entity,
                Solicitud.class));
    }
}
