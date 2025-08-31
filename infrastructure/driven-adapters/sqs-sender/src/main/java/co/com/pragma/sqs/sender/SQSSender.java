package co.com.pragma.sqs.sender;

import co.com.pragma.model.messagesender.MessageSender;
import co.com.pragma.model.messagesender.gateways.MessageSenderRepository;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<String> send(String message) {
        return Mono.fromCallable(() -> buildRequest(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<String> sendMessage(MessageSender messageSender) {
        return Mono.fromCallable(() -> toJson(messageSender))
                .flatMap(this::send);
    }

    private String toJson(MessageSender messageSender) {
        try {
            return objectMapper.writeValueAsString(messageSender);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializando MessageSender", e);
        }
    }
}
