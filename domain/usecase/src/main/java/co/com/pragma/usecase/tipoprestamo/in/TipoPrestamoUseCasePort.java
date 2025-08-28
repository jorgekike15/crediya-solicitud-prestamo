package co.com.pragma.usecase.tipoprestamo.in;

import co.com.pragma.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoPrestamoUseCasePort {

    Mono<TipoPrestamo> consultTipoPrestamoById(int id);

    Flux<TipoPrestamo> consultAllTipoPrestamos();
}
