package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.cliente.gateway.ClienteGateway;
import co.com.pragma.model.messagesender.MessageSender;
import co.com.pragma.model.messagesender.gateways.MessageSenderRepository;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.tipoprestamo.TipoPrestamo;
import co.com.pragma.usecase.solicitud.in.SolicitudUseCasePort;
import co.com.pragma.usecase.tipoprestamo.in.TipoPrestamoUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SolicitudUseCase implements SolicitudUseCasePort {

    private final SolicitudRepository solicitudRepository;
    private final ClienteGateway clienteGateway;
    private final TipoPrestamoUseCasePort tipoPrestamoUseCasePort;
    private final MessageSenderRepository messageSenderRepository;

    @Override
    public Mono<Solicitud> crearSolicitud(Solicitud solicitud, String token) {
        return clienteGateway.usuarioExist(solicitud.getDocumentoIdentificacion(), token)
                .flatMap(userValidation -> {
                    if (userValidation.isExist()) {
                        return solicitudRepository.saveSolicitud(solicitud);
                    }
                    return Mono.error(new IllegalArgumentException(userValidation.getMessage()));
                });
    }

    @Override
    public Flux<Solicitud> findAllSolicitudes() {
        return solicitudRepository.findAllSolicitudes();
    }

    @Override
    public Flux<Solicitud> findSolicitudPendienteRechazadaRevision() {
        List<Integer> codigosEstados = List.of(
                EstadoSolicitud.PENDIENTE_REVISION.getCodigo(),
                EstadoSolicitud.REVISION_MANUAL.getCodigo(),
                EstadoSolicitud.RECHAZADA.getCodigo()
        );

        Mono<Map<Integer, TipoPrestamo>> tipoPrestamoMapMono = tipoPrestamoUseCasePort.consultAllTipoPrestamos()
                .collectMap(TipoPrestamo::getId);

        return tipoPrestamoMapMono.flatMapMany(tipoPrestamoMap ->
                solicitudRepository.findSolicitudByEstadoIn(codigosEstados)
                        .map(solicitud -> {
                            TipoPrestamo tipo = tipoPrestamoMap.get(Integer.valueOf(solicitud.getIdTipoPrestamo()));
                            if (tipo != null) {
                                solicitud.setTasaInteres(tipo.getTasa_interes());
                            }
                            return solicitud;
                        })
        );
    }


    @Override
    public Mono<Boolean> gestionarSolicitud(Integer idEstado, Integer idSolicitud) {
        return solicitudRepository.updateSolicitud(idEstado, idSolicitud)
                .flatMap(actualizado -> {
                    if (actualizado.intValue() > 0) {
                        return solicitudRepository.findById(idSolicitud)
                                .flatMap(solicitud -> {
                                    MessageSender message = new MessageSender();
                                    String estado = String.valueOf(EstadoSolicitud.fromCodigo(idEstado.intValue()));
                                    message.setEmail(solicitud.getEmail());
                                    message.setMessage("Su solicitud de préstamo ha sido "
                                            + estado.toString().replace("_", " ").toLowerCase() + ".");
                                    return messageSenderRepository.sendMessage(message);
                                }).thenReturn(true);
                    }
                    return Mono.just(false);
                });
    }


    @Override
    public Mono<Solicitud> findById(Integer idSolicitud) {
        return solicitudRepository.findById(idSolicitud);
    }

}
