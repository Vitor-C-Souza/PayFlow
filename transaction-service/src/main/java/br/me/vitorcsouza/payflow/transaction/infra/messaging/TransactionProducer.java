package br.me.vitorcsouza.payflow.transaction.infra.messaging;

import br.me.vitorcsouza.payflow.transaction.domain.event.TransactionCreatedEvent;
import br.me.vitorcsouza.payflow.transaction.infra.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TransactionProducer {
    private final RabbitTemplate rabbitTemplate;

    public void publishTransactionCreated(TransactionCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                "transaction.exchange",
                "transaction.created",
                event
        );
    }
}
