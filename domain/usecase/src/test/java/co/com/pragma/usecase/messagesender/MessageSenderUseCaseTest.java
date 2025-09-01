package co.com.pragma.usecase.messagesender;

import co.com.pragma.model.messagesender.MessageSender;
import co.com.pragma.model.messagesender.gateways.MessageSenderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MessageSenderUseCaseTest {

    private MessageSenderRepository messageSenderRepository;
    private MessageSenderUseCase messageSenderUseCase;

    @BeforeEach
    void setUp() {
        messageSenderRepository = mock(MessageSenderRepository.class);
        messageSenderUseCase = new MessageSenderUseCase(messageSenderRepository);
    }

    @Test
    void sendMessage_delegaEnRepositorio_yRetornaResultado() {
        MessageSender messageSender = new MessageSender();
        String expectedResponse = "Mensaje enviado";
        when(messageSenderRepository.sendMessage(any(MessageSender.class)))
                .thenReturn(Mono.just(expectedResponse));

        StepVerifier.create(messageSenderUseCase.sendMessage(messageSender))
                .expectNext(expectedResponse)
                .verifyComplete();

        verify(messageSenderRepository).sendMessage(messageSender);
    }
}
