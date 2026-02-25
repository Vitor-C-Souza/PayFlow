package br.me.vitorcsouza.payflow.transaction.domain.event;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionCreatedEvent(
        UUID transactionId,
        UUID walletId,
        String type,
        BigDecimal amount
) {
}
