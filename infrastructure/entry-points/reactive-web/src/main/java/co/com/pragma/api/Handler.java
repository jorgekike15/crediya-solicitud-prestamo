package co.com.pragma.api;

import co.com.pragma.api.dto.CreateSolicitudDTO;
import co.com.pragma.api.dto.SolicitudDTO;
import co.com.pragma.api.dto.SolicitudResponseDTO;
import co.com.pragma.api.dto.SolicitudResponseGenericDTO;
import co.com.pragma.api.mapper.SolicitudDTOMapper;
import co.com.pragma.usecase.messagesender.in.MessageSenderUseCasePort;
import co.com.pragma.usecase.solicitud.in.SolicitudUseCasePort;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);
    private final ResourceBundle bundle = ResourceBundle.getBundle("log4j2");
    private final SolicitudUseCasePort solicitudUseCasePort;
    private final SolicitudDTOMapper solicitudDTOMapper;
    private final Validator validator;
    private final TransactionalOperator transactionalOperator;

    public Mono<ServerResponse> listenGETSaveSolicitud(ServerRequest serverRequest) {
        if (log.isTraceEnabled()) {
            log.trace(MessageFormat.format(bundle.getString("log.method.start"), "listenGETSaveSolicitud"));
        }
        return Mono.justOrEmpty(serverRequest.headers().firstHeader("Authorization"))
                .flatMap(token -> serverRequest.bodyToMono(CreateSolicitudDTO.class)
                        .doOnNext(request -> log.debug(MessageFormat.format(
                                bundle.getString("log.payload.received"), request)))
                        .flatMap(this::validacion)
                        .doOnNext(valid -> log.trace(bundle.getString("log.payload.valid")))
                        .map(solicitudDTOMapper::toModelCreate)
                        .doOnNext(domain -> log.debug(MessageFormat.format(bundle
                                .getString("log.domain.generated"), domain)))
                        .flatMap(solicitud -> solicitudUseCasePort.crearSolicitud(solicitud, token)
                                .as(transactionalOperator::transactional))
                        .map(solicitudDTOMapper::toResponse)
                        .doOnSuccess(saved -> log.info(MessageFormat.format(
                                bundle.getString("log.solicitud.created"), saved)))
                        .doOnError(error -> log.error(bundle.getString("log.solicitud.create.error"), error))
                        .flatMap(saved -> {
                            log.trace(MessageFormat.format(bundle.getString("log.response.building"), saved));
                            return ServerResponse.status(org.springframework.http.HttpStatus.CREATED).bodyValue(saved);
                        })
                        .doFinally(signalType -> log.info(
                                MessageFormat.format(bundle.getString("log.method.end"),
                                        "listenGETSaveSolicitud", signalType)
                        ))
                );
    }

    public Mono<ServerResponse> listenGETGetAllSolicitudes(ServerRequest serverRequest) {
        log.info("Inicio de método: listenGETGetAllSolicitudes");
        return ServerResponse.ok()
                .body(solicitudUseCasePort.findAllSolicitudes().map(solicitudDTOMapper::toResponse), SolicitudResponseDTO.class)
                .doOnError(e -> log.error("Error al consultar todas las solicitudes", e))
                .doFinally(signalType -> log.info("Fin de método: listenGETGetAllSolicitudes (señal: {})", signalType));
    }



    public Mono<ServerResponse> listenGETGetSolicitud(ServerRequest serverRequest) {
        if (log.isTraceEnabled()) {
            log.trace(MessageFormat.format(bundle.getString("log.method.start"), "listenGETGetSolicitud"));
        }
        int page = serverRequest.queryParam("page")
                .map(Integer::parseInt)
                .orElse(0);
        int size = serverRequest.queryParam("size")
                .map(Integer::parseInt)
                .orElse(10);

        return ServerResponse.ok()
                .body(solicitudUseCasePort.findSolicitudPendienteRechazadaRevision(page, size)
                        .map(solicitudDTOMapper::toResponse), SolicitudResponseDTO.class)
                .doOnError(e -> log.error(MessageFormat.format(bundle.getString("log.err.consult.all"), e)))
                .doFinally(signalType -> log.info(
                        MessageFormat.format(bundle.getString("log.method.end"),
                                "listenGETGetSolicitud", signalType)
                ));
    }

    public Mono<ServerResponse> listenPUTGestionarSolicitud(ServerRequest serverRequest) {
        if (log.isTraceEnabled()) {
            log.trace(MessageFormat.format(bundle.getString("log.method.start"), "listenPUTGestionarSolicitud"));
        }

        return serverRequest.bodyToMono(SolicitudDTO.class)
                .flatMap(solicitudDTO ->
                        solicitudUseCasePort.gestionarSolicitud(solicitudDTO.idEstado(), solicitudDTO.id())
                                .flatMap(solicitudUpdated -> {
                                    String message;
                                    if(Boolean.TRUE.equals(solicitudUpdated)) {
                                        message = bundle.getString("log.solicitud.updated");
                                    }else{
                                        message = MessageFormat.format(
                                                bundle.getString("log.solicitud.notupdated"), solicitudDTO.id());
                                    }

                                    log.info(message, solicitudUpdated);
                                    return ServerResponse.ok().bodyValue(
                                            new SolicitudResponseGenericDTO(Boolean.TRUE.equals(solicitudUpdated), message)
                                    );
                                })
                                .doOnError(e -> log.error(bundle.getString("log.solicitud.update.error"), e))
                                .doFinally(signalType -> log.info(
                                        MessageFormat.format(bundle.getString("log.method.end"),
                                                "listenPUTGestionarSolicitud", signalType)
                                ))
                );
    }

    private Mono<CreateSolicitudDTO> validacion(CreateSolicitudDTO request) {
        Set<ConstraintViolation<CreateSolicitudDTO>> violaciones = validator.validate(request);
        if (!violaciones.isEmpty()) {
            String errorMessage = violaciones.stream()
                    .map(violation -> violation.getPropertyPath() + ": " +
                            violation.getMessage())
                    .collect(Collectors.joining(", "));
            return Mono.error(new ValidationException(errorMessage));
        }
        return Mono.just(request);
    }
}
