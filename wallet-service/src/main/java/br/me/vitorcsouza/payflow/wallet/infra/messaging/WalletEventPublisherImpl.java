package br.me.vitorcsouza.payflow.wallet.infra.messaging;

import br.me.vitorcsouza.payflow.wallet.domain.event.WalletEventPublisher;
import br.me.vitorcsouza.payflow.wallet.domain.event.WalletTransactionEvent;
import br.me.vitorcsouza.payflow.wallet.infra.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletEventPublisherImpl implements WalletEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishTransaction(WalletTransactionEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );
    }
}
