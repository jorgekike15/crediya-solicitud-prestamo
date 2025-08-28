package co.com.pragma.api;

import co.com.pragma.api.dto.CreateSolicitudDTO;
import co.com.pragma.api.dto.SolicitudResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenGETSaveSolicitud",
                    operation = @Operation(
                            operationId = "saveSolicitud",
                            summary = "Crear una nueva solicitud",
                            description = "Crea una nueva solicitud a partir del payload recibido",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = CreateSolicitudDTO.class,
                                                    example = """
                                                            {
                                                                "monto": "5000.0",
                                                                "plazo": "12",
                                                                "email": "usuario@correo.com",
                                                                "fechaSolicitud": "2024-06-10",
                                                                "idTipoPrestamo": "2",
                                                                "documentoIdentificacion": "430"
                                                            }"""
                                            )

                                    )),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud creada exitosamente",
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = SolicitudResponseDTO.class,
                                                            example = """
                                                                    {
                                                                        "monto": "5000.0",
                                                                        "plazo": "12",
                                                                        "email": "usuario@correo.com",
                                                                        "estadoSolicitud": "PENDIENTE",
                                                                        "fechaSolicitud": "2024-06-10",
                                                                        "tipoPrestamo": "VEHICULO",
                                                                        "documentoIdentificacion": "44"
                                                                    }""")
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGETGetSolicitud",
                    operation = @Operation(
                            operationId = "findSolicitudPendienteRechazadaRevision",
                            summary = "Consultar solicitud pendiente, rechazada o en revisión",
                            description = "Consulta las solicitudes que se encuentran en estados pendiente, rechazada o en revisión",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitudes obtenidas exitosamente",
                                            content = @Content(
                                                    schema = @Schema(
                                                            example = """
                                                                    [
                                                                         {
                                                                             "monto": "5000.0",
                                                                             "plazo": "12",
                                                                             "email": "usuario@correo.com",
                                                                             "estadoSolicitud": "REVISION_MANUAL",
                                                                             "fechaSolicitud": "2024-06-10",
                                                                             "tipoPrestamo": "VEHICULO",
                                                                             "documentoIdentificacion": "123456789"
                                                                         },
                                                                         ....""")
                                            )
                                    )
                            }
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/solicitud"), handler::listenGETSaveSolicitud)
                .andRoute(GET("/api/v1/solicitud/all"), handler::listenGETGetAllSolicitudes)
                .andRoute(GET("/api/v1/solicitud"), handler::listenGETGetSolicitud);
    }
}
