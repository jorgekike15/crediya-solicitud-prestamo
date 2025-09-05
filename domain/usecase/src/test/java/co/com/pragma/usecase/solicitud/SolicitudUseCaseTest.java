package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.cliente.Cliente;
import co.com.pragma.model.cliente.UserDocumentValidationResponse;
import co.com.pragma.model.cliente.gateway.ClienteGateway;
import co.com.pragma.model.messagesender.gateways.MessageSenderRepository;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.usecase.tipoprestamo.in.TipoPrestamoUseCasePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SolicitudUseCaseTest {

//    private static final String token = "dummyToken";
//    private SolicitudRepository solicitudRepository;
//    private ClienteGateway clienteGateway;
//    private SolicitudUseCase solicitudUseCase;
//    private TipoPrestamoUseCasePort tipoPrestamoUseCasePort;
//    private MessageSenderRepository messageSenderRepository;
//
//    @BeforeEach
//    void setUp() {
//        solicitudRepository = mock(SolicitudRepository.class);
//        clienteGateway = mock(ClienteGateway.class);
//        tipoPrestamoUseCasePort = mock(TipoPrestamoUseCasePort.class);
//        messageSenderRepository = mock(MessageSenderRepository.class);
//        solicitudUseCase = new SolicitudUseCase(
//                solicitudRepository,
//                clienteGateway,
//                tipoPrestamoUseCasePort,
//                messageSenderRepository);
//    }
//
//    @Test
//    void crearSolicitud_clienteExisteYTipoPrestamoValido_guardaSolicitud() {
//        Solicitud solicitud = new Solicitud();
//        solicitud.setDocumentoIdentificacion("123");
//        solicitud.setIdTipoPrestamo("1");
//
//        UserDocumentValidationResponse response = new UserDocumentValidationResponse(true, "");
//        TipoPrestamo tipoPrestamo = TipoPrestamo.builder().id(1).tasa_interes(0.15).validacion_automatica(0).build();
//
//        when(clienteGateway.usuarioExist("123", token)).thenReturn(Mono.just(response));
//        when(tipoPrestamoUseCasePort.consultTipoPrestamoById(1)).thenReturn(Mono.just(tipoPrestamo));
//        when(solicitudRepository.saveSolicitud(solicitud)).thenReturn(Mono.just(solicitud));
//
//        StepVerifier.create(solicitudUseCase.crearSolicitud(solicitud, token))
//                .expectNext(solicitud)
//                .verifyComplete();
//
//        verify(solicitudRepository).saveSolicitud(solicitud);
//    }
//
//    @Test
//    void crearSolicitud_clienteNoExiste_lanzaError() {
//        Solicitud solicitud = new Solicitud();
//        solicitud.setDocumentoIdentificacion("123");
//        solicitud.setIdTipoPrestamo("1");
//
//        UserDocumentValidationResponse response = new UserDocumentValidationResponse(false, "No existe");
//
//        when(clienteGateway.usuarioExist("123", token)).thenReturn(Mono.just(response));
//
//        StepVerifier.create(solicitudUseCase.crearSolicitud(solicitud, token))
//                .expectError(IllegalArgumentException.class)
//                .verify();
//
//        verify(solicitudRepository, never()).saveSolicitud(any());
//    }
//
//    @Test
//    void crearSolicitud_validacionAutomaticaActiva_ejecutaFlujoAutomatico() throws Exception {
//        Solicitud solicitud = new Solicitud();
//        solicitud.setDocumentoIdentificacion("123");
//        solicitud.setIdTipoPrestamo("1");
//        solicitud.setMonto("5000");
//        solicitud.setPlazo("12");
//
//        UserDocumentValidationResponse response = new UserDocumentValidationResponse(true, "");
//        TipoPrestamo tipoPrestamo = TipoPrestamo.builder().id(1).tasa_interes(0.15).validacion_automatica(1).build();
//
//        when(clienteGateway.usuarioExist("123", token)).thenReturn(Mono.just(response));
//        when(tipoPrestamoUseCasePort.consultTipoPrestamoById(1)).thenReturn(Mono.just(tipoPrestamo));
//        when(solicitudRepository.saveSolicitud(solicitud)).thenReturn(Mono.just(solicitud));
//
//        when(solicitudRepository.findAllSolicitudesByDocument("123")).thenReturn(Flux.empty());
//        Cliente cliente = new Cliente();
//        cliente.setDocumentoIdentificacion("123");
//        cliente.setSalarioBase(1000.0);
//        when(clienteGateway.consultClienteByDocument("123", token)).thenReturn(Mono.just(cliente));
//        when(messageSenderRepository.sendMessageAutoValidation(any())).thenReturn(Mono.empty());
//
//        StepVerifier.create(solicitudUseCase.crearSolicitud(solicitud, token))
//                .expectNext(solicitud)
//                .verifyComplete();
//
//        Thread.sleep(100);
//        verify(messageSenderRepository).sendMessageAutoValidation(any());
//    }
//
//    @Test
//    void crearSolicitud_validacionAutomaticaDesactivada_noEjecutaFlujoAutomatico() {
//        Solicitud solicitud = new Solicitud();
//        solicitud.setDocumentoIdentificacion("123");
//        solicitud.setIdTipoPrestamo("1");
//
//        UserDocumentValidationResponse response = new UserDocumentValidationResponse(true, "");
//        TipoPrestamo tipoPrestamo = TipoPrestamo.builder().id(1).tasa_interes(0.15).validacion_automatica(0).build();
//
//        when(clienteGateway.usuarioExist("123", token)).thenReturn(Mono.just(response));
//        when(tipoPrestamoUseCasePort.consultTipoPrestamoById(1)).thenReturn(Mono.just(tipoPrestamo));
//        when(solicitudRepository.saveSolicitud(solicitud)).thenReturn(Mono.just(solicitud));
//
//        StepVerifier.create(solicitudUseCase.crearSolicitud(solicitud, token))
//                .expectNext(solicitud)
//                .verifyComplete();
//
//        verify(messageSenderRepository, never()).sendMessageAutoValidation(any());
//    }
//
//    @Test
//    void findSolicitudPendienteRechazadaRevision_retornaSolicitudesFiltradas() {
//        TipoPrestamo tipo1 = TipoPrestamo.builder().id(1).tasa_interes(5.0).build();
//        TipoPrestamo tipo2 = TipoPrestamo.builder().id(2).tasa_interes(7.5).build();
//
//        Solicitud s1 = new Solicitud();
//        s1.setIdTipoPrestamo("1");
//        Solicitud s2 = new Solicitud();
//        s2.setIdTipoPrestamo("2");
//
//        when(tipoPrestamoUseCasePort.consultAllTipoPrestamos()).thenReturn(Flux.just(tipo1, tipo2));
//        when(solicitudRepository.findByIdEstadoInPaged(any(), anyInt(), anyInt()))
//                .thenReturn(Flux.just(s1, s2));
//
//        StepVerifier.create(solicitudUseCase.findSolicitudPendienteRechazadaRevision(0, 10))
//                .assertNext(sol -> {
//                    assert sol.getTasaInteres() == 5.0;
//                })
//                .assertNext(sol -> {
//                    assert sol.getTasaInteres() == 7.5;
//                })
//                .verifyComplete();
//
//        verify(tipoPrestamoUseCasePort).consultAllTipoPrestamos();
//        verify(solicitudRepository).findByIdEstadoInPaged(any(), anyInt(), anyInt());
//    }
//
//    @Test
//    void findAllSolicitudes_retornaTodasLasSolicitudes() {
//        Solicitud s1 = new Solicitud();
//        Solicitud s2 = new Solicitud();
//
//        when(solicitudRepository.findAllSolicitudes()).thenReturn(Flux.just(s1, s2));
//
//        StepVerifier.create(solicitudUseCase.findAllSolicitudes())
//                .expectNext(s1)
//                .expectNext(s2)
//                .verifyComplete();
//
//        verify(solicitudRepository).findAllSolicitudes();
//    }
//
//    @Test
//    void gestionarSolicitud_actualizaYEnviaMensaje_exitoso() {
//        Integer idEstado = 1;
//        Integer idSolicitud = 10;
//        Solicitud solicitud = new Solicitud();
//        solicitud.setEmail("test@email.com");
//
//        when(solicitudRepository.updateEstadoSolicitud(idEstado, idSolicitud)).thenReturn(Mono.just(1));
//        when(solicitudRepository.findById(idSolicitud)).thenReturn(Mono.just(solicitud));
//        when(messageSenderRepository.sendMessage(any())).thenReturn(Mono.empty());
//
//        StepVerifier.create(solicitudUseCase.gestionarSolicitud(idEstado, idSolicitud))
//                .expectNext(true)
//                .verifyComplete();
//
//        verify(solicitudRepository).updateEstadoSolicitud(idEstado, idSolicitud);
//        verify(solicitudRepository).findById(idSolicitud);
//        verify(messageSenderRepository).sendMessage(any());
//    }
//
//    @Test
//    void gestionarSolicitud_noActualiza_noEnviaMensaje() {
//        Integer idEstado = 1;
//        Integer idSolicitud = 10;
//
//        when(solicitudRepository.updateEstadoSolicitud(idEstado, idSolicitud)).thenReturn(Mono.just(0));
//
//        StepVerifier.create(solicitudUseCase.gestionarSolicitud(idEstado, idSolicitud))
//                .expectNext(false)
//                .verifyComplete();
//
//        verify(solicitudRepository).updateEstadoSolicitud(idEstado, idSolicitud);
//        verify(solicitudRepository, never()).findById(any());
//        verify(messageSenderRepository, never()).sendMessage(any());
//    }
//
//
//    @Test
//    void isAutomaticValidation_retornaTrueCuandoValidacionAutomaticaEsUno() throws Exception {
//        Solicitud solicitud = new Solicitud();
//        solicitud.setIdTipoPrestamo("1");
//        TipoPrestamo tipoPrestamo = TipoPrestamo.builder().id(1).validacion_automatica(1).build();
//
//        when(tipoPrestamoUseCasePort.consultTipoPrestamoById(1)).thenReturn(Mono.just(tipoPrestamo));
//
//        Method method = SolicitudUseCase.class.getDeclaredMethod("isAutomaticValidation", Solicitud.class);
//        method.setAccessible(true);
//        Mono<Boolean> result = (Mono<Boolean>) method.invoke(solicitudUseCase, solicitud);
//
//        StepVerifier.create(result)
//                .expectNext(true)
//                .verifyComplete();
//    }
//
//    @Test
//    void isAutomaticValidation_retornaFalseCuandoValidacionAutomaticaEsCero() throws Exception {
//        Solicitud solicitud = new Solicitud();
//        solicitud.setIdTipoPrestamo("1");
//        TipoPrestamo tipoPrestamo = TipoPrestamo.builder().id(1).validacion_automatica(0).build();
//
//        when(tipoPrestamoUseCasePort.consultTipoPrestamoById(1)).thenReturn(Mono.just(tipoPrestamo));
//
//        Method method = SolicitudUseCase.class.getDeclaredMethod("isAutomaticValidation", Solicitud.class);
//        method.setAccessible(true);
//        Mono<Boolean> result = (Mono<Boolean>) method.invoke(solicitudUseCase, solicitud);
//
//        StepVerifier.create(result)
//                .expectNext(false)
//                .verifyComplete();
//    }
//
//    @Test
//    void processAutomaticValidation_enviaMensajeAutoValidation() throws Exception {
//        Solicitud solicitud = new Solicitud();
//        solicitud.setId(100);
//        solicitud.setDocumentoIdentificacion("123");
//        solicitud.setIdTipoPrestamo("1");
//        solicitud.setMonto("5000");
//        solicitud.setPlazo("12");
//
//        // Solicitudes aprobadas simuladas
//        Solicitud aprobada = new Solicitud();
//        aprobada.setIdEstado(2); // Estado APROBADA
//
//        Cliente cliente = new Cliente();
//        cliente.setDocumentoIdentificacion("123");
//        cliente.setSalarioBase(20000.0);
//
//        when(solicitudRepository.findAllSolicitudesByDocument("123"))
//                .thenReturn(Flux.just(aprobada));
//        when(clienteGateway.consultClienteByDocument("123", token))
//                .thenReturn(Mono.just(cliente));
//        when(tipoPrestamoUseCasePort.consultTipoPrestamoById(1))
//                .thenReturn(Mono.just(TipoPrestamo.builder().id(1).tasa_interes(0.15).build()));
//        when(messageSenderRepository.sendMessageAutoValidation(any()))
//                .thenReturn(Mono.empty());
//
//        Method method = SolicitudUseCase.class.getDeclaredMethod("processAutomaticValidation", Solicitud.class, String.class);
//        method.setAccessible(true);
//        method.invoke(solicitudUseCase, solicitud, token);
//
//        Thread.sleep(100);
//
//        verify(messageSenderRepository).sendMessageAutoValidation(any());
//    }

}