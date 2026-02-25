package br.me.vitorcsouza.payflow.transaction.infra.messaging;

import br.me.vitorcsouza.payflow.transaction.domain.event.TransactionApprovedEvent;
import br.me.vitorcsouza.payflow.transaction.domain.event.TransactionCreatedEvent;
import br.me.vitorcsouza.payflow.transaction.domain.event.TransactionRejectedEvent;
import br.me.vitorcsouza.payflow.transaction.domain.event.WalletTransactionEvent;
import br.me.vitorcsouza.payflow.transaction.domain.model.Transaction;
import br.me.vitorcsouza.payflow.transaction.domain.model.TransactionStatus;
import br.me.vitorcsouza.payflow.transaction.domain.model.TransactionType;
import br.me.vitorcsouza.payflow.transaction.domain.repository.TransactionRepository;
import br.me.vitorcsouza.payflow.transaction.domain.service.TransactionService;
import br.me.vitorcsouza.payflow.transaction.infra.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

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
            transaction.setStatus(TransactionStatus.PENDING);

            transactionRepository.save(transaction);

            TransactionCreatedEvent antifraudEvent =
                    new TransactionCreatedEvent(
                            transaction.getId(),
                            transaction.getWalletId(),
                            transaction.getType().name(),
                            transaction.getAmount()
                    );

            transactionService.publishTransactionCreated(antifraudEvent);

            log.info("Transaction saved with id: {}", transaction.getId());
        } catch (Exception e) {
            log.error("Error processing transaction event: {}", event.walletId(), e);
            throw e;
        }
    }

    @Recover
    public void recover(Exception e, WalletTransactionEvent event) {
        System.out.println("Failed to process event after retries: " + event.eventId());
    }


    @RabbitListener(queues = RabbitConfig.TRANSACTION_APPROVED_QUEUE)
    public void handleApproved(TransactionApprovedEvent event) {
        transactionService.approveTransaction(event.transactionId());
    }

    @RabbitListener(queues = RabbitConfig.TRANSACTION_REJECTED_QUEUE)
    public void handleRejected(TransactionRejectedEvent event) {
        transactionService.rejectTransaction(event.transactionId(), event.reason());
    }

}
