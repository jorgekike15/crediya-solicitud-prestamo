package co.com.pragma.model.tipoprestamo.gateways;

import co.com.pragma.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {

    Flux<TipoPrestamo> findAllTipoPrestamos();

    Mono<TipoPrestamo> findTipoPrestamoById(int id);
}
