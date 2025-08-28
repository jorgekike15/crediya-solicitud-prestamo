package co.com.pragma.usecase.tipoprestamo;

import co.com.pragma.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.pragma.usecase.tipoprestamo.in.TipoPrestamoUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TipoPrestamoUseCase implements TipoPrestamoUseCasePort {

    private final TipoPrestamoRepository tipoPrestamoRepository;

    @Override
    public Mono<TipoPrestamo> consultTipoPrestamoById(int id) {
        return tipoPrestamoRepository.findTipoPrestamoById(id);
    }

    @Override
    public Flux<TipoPrestamo> consultAllTipoPrestamos() {
        return tipoPrestamoRepository.findAllTipoPrestamos();
    }


}
