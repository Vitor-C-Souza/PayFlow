package br.me.vitorcsouza.payflow.antifraud_service.infra.messaging;

import br.me.vitorcsouza.payflow.antifraud_service.domain.events.TransactionApprovedEvent;
import br.me.vitorcsouza.payflow.antifraud_service.domain.events.TransactionRejectedEvent;
import br.me.vitorcsouza.payflow.antifraud_service.infra.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AntifraudPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void approve(UUID transactionId) {

        TransactionApprovedEvent event =
                new TransactionApprovedEvent(transactionId);

        rabbitTemplate.convertAndSend(
                RabbitConfig.TRANSACTION_EXCHANGE,
                "transaction.approved",
                event
        );
    }

    public void reject(UUID transactionId, String reason) {

        TransactionRejectedEvent event =
                new TransactionRejectedEvent(transactionId, reason);

        rabbitTemplate.convertAndSend(
                RabbitConfig.TRANSACTION_EXCHANGE,
                "transaction.rejected",
                event
        );
    }
}
