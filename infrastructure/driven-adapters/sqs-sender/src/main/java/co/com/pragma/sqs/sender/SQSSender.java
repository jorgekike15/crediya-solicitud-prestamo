package co.com.pragma.sqs.sender;

import co.com.pragma.model.messagesender.MessageAutoValidation;
import co.com.pragma.model.messagesender.MessageSender;
import co.com.pragma.model.messagesender.gateways.MessageSenderRepository;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements MessageSenderRepository {

    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${adapter.sqs.queue-notification-url}")
    private String notificacionQueueUrl;

    @Value("${adapter.sqs.queue-automatic-validation-url}")
    private String validacionQueueUrl;

    @Value("${adapter.sqs.queue-contador-solicitudes-url}")
    private String contadorSolicitudesQueueUrl;

    public Mono<String> send(String message, String urlQueue) {
        log.info("Inicio del envio de mensaje a la cola SQS: {} con mensaje: {}", urlQueue, message);
        return Mono.fromCallable(() -> buildRequest(message, urlQueue))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message, String urlQueue) {
        return SendMessageRequest.builder()
                .queueUrl(urlQueue)
                .messageBody(message)
                .build();
    }


    @Override
    public Mono<String> sendMessage(MessageSender messageSender) {
        return Mono.fromCallable(() -> toJson(messageSender)).flatMap(message ->
        send(message, notificacionQueueUrl));
    }

    @Override
    public Mono<String> sendMessageAutoValidation(MessageAutoValidation messageAutoValidation) {
        return Mono.fromCallable(() -> toJson(messageAutoValidation)).flatMap(message ->
                send(message, validacionQueueUrl ));
    }

    @Override
    public Mono<String> sendMessageAutoIncrementalApproved(MessageSender messageSender) {
        return Mono.fromCallable(() -> toJson(messageSender)).flatMap(message ->
                send(message, contadorSolicitudesQueueUrl));
    }

    private <T> String toJson(T message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializando MessageSender", e);
        }
    }
}
