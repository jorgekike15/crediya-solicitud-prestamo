package co.com.pragma.model.messagesender.gateways;

import co.com.pragma.model.messagesender.MessageAutoValidation;
import co.com.pragma.model.messagesender.MessageSender;
import reactor.core.publisher.Mono;

public interface MessageSenderRepository {

    Mono<String> sendMessage(MessageSender messageSender);

    Mono<String> sendMessageAutoValidation(MessageAutoValidation messageAutoValidation);

    Mono<String> sendMessageAutoIncrementalApproved(MessageSender messageSender);

}
