package co.com.pragma.usecase.messagesender;

import co.com.pragma.model.messagesender.MessageSender;
import co.com.pragma.model.messagesender.gateways.MessageSenderRepository;
import co.com.pragma.usecase.messagesender.in.MessageSenderUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MessageSenderUseCase implements MessageSenderUseCasePort {

    private final MessageSenderRepository messageSenderRepository;

    @Override
    public Mono<String> sendMessage(MessageSender messageSender) {
        return messageSenderRepository.sendMessage(messageSender);
    }
}
