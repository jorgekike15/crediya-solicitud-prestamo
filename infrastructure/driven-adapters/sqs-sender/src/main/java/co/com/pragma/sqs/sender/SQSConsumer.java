package co.com.pragma.sqs.sender;

import co.com.pragma.model.messagesender.MessageSender;
import co.com.pragma.model.messagesender.gateways.MessageSenderRepository;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.sqs.sender.enums.EstadoSolicitud;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class SQSConsumer {

    private final SolicitudRepository solicitudRepository;
    private final ObjectMapper mapper = new ObjectMapper();
    private final MessageSenderRepository messageSenderRepository;

    public Mono<Void> procesarResultado(String mensaje) {
        log.info("Mensaje recibido en ValidacionConsumer: {}", mensaje);

        try {
            JsonNode json = mapper.readTree(mensaje);

            Integer idSolicitud = json.get("idSolicitud").asInt();
            String estado = json.get("estado").asText();

            log.info("Parsed mensaje -> idSolicitud: {}, decision: {}", idSolicitud, estado);

            Integer codigoEstado = switch (estado) {
                case "APROBADA" -> 2;
                case "RECHAZADA" -> 3;
                default -> 1;
            };

            if (EstadoSolicitud.APROBADA.getCodigo() == codigoEstado) {
                solicitudRepository.findById(idSolicitud).flatMap(solicitudAprobada ->
                        messageSenderRepository.sendMessageAutoIncrementalApproved(
                                new MessageSender("", EstadoSolicitud.APROBADA.toString(), solicitudAprobada.getMonto()))
                        ).subscribe();
            }

            return solicitudRepository.updateEstadoSolicitud(codigoEstado, idSolicitud)
                    .flatMap(rowsUpdated -> {
                        if (rowsUpdated > 0) {
                            log.info("Estado actualizado correctamente para solicitud {}", idSolicitud);
                            return Mono.just(rowsUpdated);
                        }
                        log.info("No se actualizo actualizado correctamente solicitud {}", idSolicitud);
                        return Mono.error(new RuntimeException("No se encontró la solicitud con id: " + idSolicitud));

                    })
                    .doOnError(err -> log.error("Error actualizando estado para solicitud {} con estado {}", idSolicitud, estado, err))
                    .then();

        } catch (Exception e) {
            log.error("Error procesando mensaje: {}", mensaje, e);
            return Mono.error(e);
        }
    }
}
