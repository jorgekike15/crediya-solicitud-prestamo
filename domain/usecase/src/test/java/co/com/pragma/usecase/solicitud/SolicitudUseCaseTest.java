package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.cliente.gateway.ClienteGateway;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SolicitudUseCaseTest {

    private SolicitudRepository solicitudRepository;
    private ClienteGateway clienteGateway;
    private SolicitudUseCase solicitudUseCase;

    @BeforeEach
    void setUp() {
        solicitudRepository = mock(SolicitudRepository.class);
        clienteGateway = mock(ClienteGateway.class);
        solicitudUseCase = new SolicitudUseCase(solicitudRepository, clienteGateway);
    }

    @Test
    void crearSolicitud_clienteExiste_guardaSolicitud() {
        Solicitud solicitud = new Solicitud();
        solicitud.setDocumentoIdentificacion("123");

        when(clienteGateway.usuarioExist("123")).thenReturn(Mono.just(true));
        when(solicitudRepository.saveSolicitud(solicitud)).thenReturn(Mono.just(solicitud));

        StepVerifier.create(solicitudUseCase.crearSolicitud(solicitud))
                .expectNext(solicitud)
                .verifyComplete();

        verify(solicitudRepository).saveSolicitud(solicitud);
    }

    @Test
    void crearSolicitud_clienteNoExiste_lanzaError() {
        Solicitud solicitud = new Solicitud();
        solicitud.setDocumentoIdentificacion("123");

        when(clienteGateway.usuarioExist("123")).thenReturn(Mono.just(false));

        StepVerifier.create(solicitudUseCase.crearSolicitud(solicitud))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(solicitudRepository, never()).saveSolicitud(any());
    }

    @Test
    void findAllSolicitudes_retornaFlux() {
        Solicitud s1 = new Solicitud();
        Solicitud s2 = new Solicitud();
        when(solicitudRepository.findAllSolicitudes()).thenReturn(Flux.just(s1, s2));

        StepVerifier.create(solicitudUseCase.findAllSolicitudes())
                .expectNext(s1)
                .expectNext(s2)
                .verifyComplete();
    }
}