package br.me.vitorcsouza.payflow.transaction.infra.messaging;

import br.me.vitorcsouza.payflow.transaction.domain.event.WalletTransactionEvent;
import br.me.vitorcsouza.payflow.transaction.domain.model.Transaction;
import br.me.vitorcsouza.payflow.transaction.domain.model.TransactionType;
import br.me.vitorcsouza.payflow.transaction.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TransactionRepository transactionRepository;

    @RabbitListener(queues = "transaction.queue")
    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void consume(WalletTransactionEvent event) {

        try {
            log.info("Received event {}", event.eventId());

            if (transactionRepository.existsByEventId(event.eventId())) {
                log.warn("Event already processed {}", event.eventId());
                return;
            }

            Transaction transaction = new Transaction();
            transaction.setWalletId(event.walletId());
            transaction.setAmount(event.amount());
            transaction.setDescription(event.description());
            transaction.setType(TransactionType.valueOf(event.type()));
            transaction.setCreatedAt(event.createdAt());
            transaction.setEventId(event.eventId());

            transactionRepository.save(transaction);

            log.info("Transaction saved with id: {}", event.walletId());
        } catch (Exception e) {
            log.error("Error processing transaction event: {}", event.walletId(), e);
            throw e;
        }
    }

    @Recover
    public void recover(Exception e, WalletTransactionEvent event) {
        System.out.println("Failed to process event after retries: " + event.eventId());
    }

}
