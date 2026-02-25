package br.me.vitorcsouza.payflow.antifraud_service.infra.messaging;

import br.me.vitorcsouza.payflow.antifraud_service.domain.events.TransactionCreatedEvent;
import br.me.vitorcsouza.payflow.antifraud_service.domain.service.AntifraudService;
import br.me.vitorcsouza.payflow.antifraud_service.infra.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionConsumer {

    private final AntifraudService antifraudService;

    @RabbitListener(queues = RabbitConfig.TRANSACTION_CREATED_QUEUE)
    public void receive(TransactionCreatedEvent event) {
        antifraudService.analyze(event);
    }
}
