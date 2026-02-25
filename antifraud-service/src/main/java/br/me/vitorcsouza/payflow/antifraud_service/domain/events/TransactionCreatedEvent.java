package br.me.vitorcsouza.payflow.antifraud_service.domain.events;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionCreatedEvent (
        UUID transactionId,
        UUID walletId,
        String type,
        BigDecimal amount
) {
}
