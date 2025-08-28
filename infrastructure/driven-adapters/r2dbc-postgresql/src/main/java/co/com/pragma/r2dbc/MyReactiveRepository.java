package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.SolicitudEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;


public interface MyReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Integer>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

    Flux<SolicitudEntity> findByIdEstadoIn(List<Integer> estados);

}
