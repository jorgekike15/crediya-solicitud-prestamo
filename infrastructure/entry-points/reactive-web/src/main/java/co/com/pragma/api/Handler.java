package co.com.pragma.api;

import co.com.pragma.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);
    private final SolicitudUseCase solicitudUseCase;

    public Mono<ServerResponse> listenGETSaveSolicitud(ServerRequest serverRequest) {
        log.info("Inicio de método: listenGETSaveSolicitud ");
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETGetAllSolicitudes(ServerRequest serverRequest) {
        log.info("Inicio de método: listenGETGetAllSolicitudes ");
        return ServerResponse.ok().body(solicitudUseCase.findAllSolicitudes().map);
    }
}
