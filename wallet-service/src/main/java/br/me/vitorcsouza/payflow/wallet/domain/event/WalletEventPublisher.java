package br.me.vitorcsouza.payflow.wallet.domain.event;

import br.me.vitorcsouza.payflow.wallet.infra.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishTransaction(WalletTransactionEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
    }
}
