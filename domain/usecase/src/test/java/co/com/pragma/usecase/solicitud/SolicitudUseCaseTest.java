package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.cliente.UserDocumentValidationResponse;
import co.com.pragma.model.cliente.gateway.ClienteGateway;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.usecase.tipoprestamo.in.TipoPrestamoUseCasePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SolicitudUseCaseTest {

    private SolicitudRepository solicitudRepository;
    private ClienteGateway clienteGateway;
    private SolicitudUseCase solicitudUseCase;
    private TipoPrestamoUseCasePort tipoPrestamoUseCasePort;
    private static final String token = "dummyToken";

    @BeforeEach
    void setUp() {
        solicitudRepository = mock(SolicitudRepository.class);
        clienteGateway = mock(ClienteGateway.class);
        tipoPrestamoUseCasePort = mock(TipoPrestamoUseCasePort.class);
        solicitudUseCase = new SolicitudUseCase(solicitudRepository, clienteGateway, tipoPrestamoUseCasePort);
    }

    @Test
    void crearSolicitud_clienteExiste_guardaSolicitud() {
        Solicitud solicitud = new Solicitud();
        solicitud.setDocumentoIdentificacion("123");
        UserDocumentValidationResponse response = new UserDocumentValidationResponse(true,
                "");

        when(clienteGateway.usuarioExist("123", token)).thenReturn(Mono.just(response));
        when(solicitudRepository.saveSolicitud(solicitud)).thenReturn(Mono.just(solicitud));

        StepVerifier.create(solicitudUseCase.crearSolicitud(solicitud, token))
                .expectNext(solicitud)
                .verifyComplete();

        verify(solicitudRepository).saveSolicitud(solicitud);
    }

    @Test
    void crearSolicitud_clienteNoExiste_lanzaError() {
        Solicitud solicitud = new Solicitud();
        solicitud.setDocumentoIdentificacion("123");
        UserDocumentValidationResponse response = new UserDocumentValidationResponse(false,
                "Cliente no existe");

        when(clienteGateway.usuarioExist("123", token)).thenReturn(Mono.just(response));

        StepVerifier.create(solicitudUseCase.crearSolicitud(solicitud, token))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(solicitudRepository, never()).saveSolicitud(any());
    }

    @Test
    void findSolicitudPendienteRechazadaRevision_retornaSolicitudesFiltradas() {
        TipoPrestamo tipo1 = TipoPrestamo.builder().id(1).tasa_interes(5.0).build();
        TipoPrestamo tipo2 = TipoPrestamo.builder().id(2).tasa_interes(7.5).build();

        // Mock de solicitudes
        Solicitud s1 = new Solicitud();
        s1.setIdTipoPrestamo("1");
        Solicitud s2 = new Solicitud();
        s2.setIdTipoPrestamo("2");

        List<Integer> codigosEstados = List.of(
                EstadoSolicitud.PENDIENTE_REVISION.getCodigo(),
                EstadoSolicitud.REVISION_MANUAL.getCodigo(),
                EstadoSolicitud.RECHAZADA.getCodigo()
        );

        when(tipoPrestamoUseCasePort.consultAllTipoPrestamos()).thenReturn(Flux.just(tipo1, tipo2));
        when(solicitudRepository.findSolicitudByEstadoIn(codigosEstados)).thenReturn(Flux.just(s1, s2));

        StepVerifier.create(solicitudUseCase.findSolicitudPendienteRechazadaRevision())
                .assertNext(sol -> {
                    assert sol.getTasaInteres() == 5.0;
                })
                .assertNext(sol -> {
                    assert sol.getTasaInteres() == 7.5;
                })
                .verifyComplete();

        verify(tipoPrestamoUseCasePort).consultAllTipoPrestamos();
        verify(solicitudRepository).findSolicitudByEstadoIn(codigosEstados);
    }

    @Test
    void findAllSolicitudes_retornaTodasLasSolicitudes() {
        Solicitud s1 = new Solicitud();
        Solicitud s2 = new Solicitud();

        when(solicitudRepository.findAllSolicitudes()).thenReturn(Flux.just(s1, s2));

        StepVerifier.create(solicitudUseCase.findAllSolicitudes())
                .expectNext(s1)
                .expectNext(s2)
                .verifyComplete();

        verify(solicitudRepository).findAllSolicitudes();
    }
}