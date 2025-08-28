package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.pragma.r2dbc.entity.SolicitudEntity;
import co.com.pragma.r2dbc.entity.TipoPrestamoEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class TipoPrestamoReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        TipoPrestamo,
        TipoPrestamoEntity,
        Integer,
        TipoPrestamoReactiveRepository
        > implements TipoPrestamoRepository {
    public TipoPrestamoReactiveRepositoryAdapter(TipoPrestamoReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, TipoPrestamo.class));
    }

    @Override
    public Flux<TipoPrestamo> findAllTipoPrestamos() {
        return repository.findAll().map(entity -> mapper.map(entity, TipoPrestamo.class));
    }

    @Override
    public Mono<TipoPrestamo> findTipoPrestamoById(int id) {
        return repository.findById(id).map(entity -> mapper.map(entity, TipoPrestamo.class));
    }
}
