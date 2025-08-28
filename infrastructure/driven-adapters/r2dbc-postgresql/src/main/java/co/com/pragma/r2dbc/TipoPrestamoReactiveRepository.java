package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.SolicitudEntity;
import co.com.pragma.r2dbc.entity.TipoPrestamoEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;


public interface TipoPrestamoReactiveRepository extends ReactiveCrudRepository<TipoPrestamoEntity, Integer>, ReactiveQueryByExampleExecutor<TipoPrestamoEntity> {

}
