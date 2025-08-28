package co.com.pragma.usecase.tipoprestamo;

import co.com.pragma.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.model.tipoprestamo.gateways.TipoPrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TipoPrestamoUseCaseTest {

    private TipoPrestamoRepository tipoPrestamoRepository;
    private TipoPrestamoUseCase useCase;

    @BeforeEach
    void setUp() {
        tipoPrestamoRepository = mock(TipoPrestamoRepository.class);
        useCase = new TipoPrestamoUseCase(tipoPrestamoRepository);
    }

    @Test
    void consultTipoPrestamoById_retornaTipoPrestamo() {
        TipoPrestamo tipo = TipoPrestamo.builder().id(1).nombre("Consumo").build();
        when(tipoPrestamoRepository.findTipoPrestamoById(1)).thenReturn(Mono.just(tipo));

        StepVerifier.create(useCase.consultTipoPrestamoById(1))
                .expectNextMatches(tp -> tp.getId() == 1L && tp.getNombre().equals("Consumo"))
                .verifyComplete();

        verify(tipoPrestamoRepository).findTipoPrestamoById(1);
    }

    @Test
    void consultTipoPrestamoById_noExiste() {
        when(tipoPrestamoRepository.findTipoPrestamoById(anyInt())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.consultTipoPrestamoById(99))
                .verifyComplete();

        verify(tipoPrestamoRepository).findTipoPrestamoById(99);
    }

    @Test
    void consultAllTipoPrestamos_retornaTodos() {
        TipoPrestamo tipo1 = TipoPrestamo.builder().id(1).nombre("Consumo").build();
        TipoPrestamo tipo2 = TipoPrestamo.builder().id(2).nombre("Hipotecario").build();
        when(tipoPrestamoRepository.findAllTipoPrestamos()).thenReturn(Flux.just(tipo1, tipo2));

        StepVerifier.create(useCase.consultAllTipoPrestamos())
                .expectNext(tipo1)
                .expectNext(tipo2)
                .verifyComplete();

        verify(tipoPrestamoRepository).findAllTipoPrestamos();
    }
}
