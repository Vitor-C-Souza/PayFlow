package br.me.vitorcsouza.payflow.wallet.infra.messaging;

import br.me.vitorcsouza.payflow.wallet.domain.event.TransactionApprovedEvent;
import br.me.vitorcsouza.payflow.wallet.domain.service.WalletService;
import br.me.vitorcsouza.payflow.wallet.infra.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletConsumer {

    private final WalletService walletService;

    @RabbitListener(queues = RabbitConfig.TRANSACTION_APPROVED_QUEUE)
    public void receive(TransactionApprovedEvent event) {

        walletService.applyTransaction(
                event.walletId(),
                event.type(),
                event.amount()
        );
    }
}
