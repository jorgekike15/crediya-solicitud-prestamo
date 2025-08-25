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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);
    private static final String ERROR = "Error: ";
    private final SolicitudUseCase solicitudUseCase;
    private final SolicitudDTOMapper solicitudDTOMapper;
    private final Validator validator;

    public Mono<ServerResponse> listenGETSaveSolicitud(ServerRequest serverRequest) {
        log.trace("Inicio de método: listenGETSaveSolicitud");
        return serverRequest.bodyToMono(CreateSolicitudDTO.class)
                .doOnNext(request -> log.debug("Payload recibido: {}", request))
                .flatMap(this::validacion)
                .doOnNext(valid -> log.trace("Payload validado correctamente"))
                .map(solicitudDTOMapper::toModelCreate)
                .doOnNext(domain -> log.debug("Objeto de dominio generado: {}", domain))
                .flatMap(solicitud -> solicitudUseCase.saveSolicitud(solicitud))
                .map(solicitudDTOMapper::toResponse)
                .doOnSuccess(saved -> log.info("Solicitud creada exitosamente: {}", saved))
                .doOnError(error -> log.error("Error al crear solicitud", error))
                .flatMap(saved -> {
                    log.trace("Construyendo respuesta HTTP 201 para solicitud: {}", saved);
                    return ServerResponse.status(org.springframework.http.HttpStatus.CREATED).bodyValue(saved);
                })
                .doFinally(signalType -> log.info("Fin de método listenGETSaveSolicitud (señal: {})", signalType));
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
