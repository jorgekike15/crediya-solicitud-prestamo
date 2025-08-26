package co.com.pragma.api;

import co.com.pragma.api.mapper.SolicitudDTOMapper;
import co.com.pragma.usecase.solicitud.SolicitudUseCase;
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
    private final SolicitudUseCase solicitudUseCase;
    private final SolicitudDTOMapper solicitudDTOMapper;
    private final Validator validator;
    private final TransactionalOperator transactionalOperator;

    public Mono<ServerResponse> listenGETSaveSolicitud(ServerRequest serverRequest) {
        if (log.isTraceEnabled()) {
            log.trace(MessageFormat.format(bundle.getString("log.method.start"), "listenGETSaveSolicitud"));
        }
        return serverRequest.bodyToMono(CreateSolicitudDTO.class)
                .doOnNext(request -> log.debug(MessageFormat.format(
                        bundle.getString("log.payload.received"), request)))
                .flatMap(this::validacion)
                .doOnNext(valid -> log.trace(bundle.getString("log.payload.valid")))
                .map(solicitudDTOMapper::toModelCreate)
                .doOnNext(domain -> log.debug(MessageFormat.format(bundle
                        .getString("log.domain.generated"), domain)))
                .flatMap(solicitud -> solicitudUseCase.crearSolicitud(solicitud)
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
        ));
    }

    public Mono<ServerResponse> listenGETGetAllSolicitudes(ServerRequest serverRequest) {
        log.info("Inicio de método: listenGETGetAllSolicitudes");
        return ServerResponse.ok()
                .body(solicitudUseCase.findAllSolicitudes().map(solicitudDTOMapper::toResponse), SolicitudResponseDTO.class)
                .doOnError(e -> log.error("Error al consultar todas las solicitudes", e))
                .doFinally(signalType -> log.info("Fin de método: listenGETGetAllSolicitudes (señal: {})", signalType));
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
