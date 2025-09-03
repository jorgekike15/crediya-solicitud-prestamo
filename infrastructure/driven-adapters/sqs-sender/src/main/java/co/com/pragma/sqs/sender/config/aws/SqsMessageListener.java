package co.com.pragma.sqs.sender.config.aws;

import co.com.pragma.sqs.sender.SQSConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.awspring.cloud.sqs.annotation.SqsListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsMessageListener {

    private final SQSConsumer sqsConsumer;

    @SqsListener("${adapter.sqs.queue-automatic-validation-response}")
    public void receiveMessage(String messageBody) {
        log.info("Received message from SQS: {}", messageBody);
        sqsConsumer.procesarResultado(messageBody).subscribe();
    }
}
