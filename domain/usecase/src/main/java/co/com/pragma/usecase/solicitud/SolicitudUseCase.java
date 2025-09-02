package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.cliente.gateway.ClienteGateway;
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
    public Flux<Solicitud> findAllSolicitudes(){
        return solicitudRepository.findAllSolicitudes();
    }

    @Override
    public Flux<Solicitud> findSolicitudPendienteRechazadaRevision(Integer page, Integer size) {
        List<Integer> codigosEstados = List.of(
                EstadoSolicitud.PENDIENTE_REVISION.getCodigo(),
                EstadoSolicitud.REVISION_MANUAL.getCodigo(),
                EstadoSolicitud.RECHAZADA.getCodigo()
        );

        Mono<Map<Integer, TipoPrestamo>> tipoPrestamoMapMono = tipoPrestamoUseCasePort.consultAllTipoPrestamos()
                .collectMap(TipoPrestamo::getId);

        return tipoPrestamoMapMono.flatMapMany(tipoPrestamoMap ->
                solicitudRepository.findByIdEstadoInPaged(codigosEstados, size, page * size)
                        .map(solicitud -> {
                            TipoPrestamo tipo = tipoPrestamoMap.get(Integer.valueOf(solicitud.getIdTipoPrestamo()));
                            if (tipo != null) {
                                solicitud.setTasaInteres(tipo.getTasa_interes());
                            }
                            return solicitud;
                        })
        );
    }

}
