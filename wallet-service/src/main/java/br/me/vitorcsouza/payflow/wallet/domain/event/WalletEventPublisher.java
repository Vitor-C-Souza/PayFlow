package br.me.vitorcsouza.payflow.wallet.domain.event;

import br.me.vitorcsouza.payflow.wallet.infra.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

public interface WalletEventPublisher {

    void publishTransaction(WalletTransactionEvent event);
}
