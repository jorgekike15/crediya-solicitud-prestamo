package co.com.pragma.usecase.messagesender.in;

import co.com.pragma.model.messagesender.MessageSender;
import reactor.core.publisher.Mono;

public interface MessageSenderUseCasePort {

    Mono<String> sendMessage(MessageSender messageSender);
}
