package br.me.vitorcsouza.payflow.transaction.infra.messaging;

import br.me.vitorcsouza.payflow.transaction.domain.event.WalletTransactionEvent;
import br.me.vitorcsouza.payflow.transaction.domain.model.Transaction;
import br.me.vitorcsouza.payflow.transaction.domain.model.TransactionType;
import br.me.vitorcsouza.payflow.transaction.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TransactionRepository transactionRepository;

    @RabbitListener(queues = "transaction.queue")
    public void consume(WalletTransactionEvent event) {

        Transaction transaction = new Transaction();
        transaction.setWalletId(event.walletId());
        transaction.setAmount(event.amount());
        transaction.setDescription(event.description());
        transaction.setType(TransactionType.valueOf(event.type()));

        transactionRepository.save(transaction);
    }

}
