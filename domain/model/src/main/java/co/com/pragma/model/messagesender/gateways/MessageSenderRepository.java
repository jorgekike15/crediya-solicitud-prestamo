package co.com.pragma.model.messagesender.gateways;

import co.com.pragma.model.messagesender.MessageSender;
import reactor.core.publisher.Mono;

public interface MessageSenderRepository {

    Mono<String> sendMessage(MessageSender messageSender);

}
