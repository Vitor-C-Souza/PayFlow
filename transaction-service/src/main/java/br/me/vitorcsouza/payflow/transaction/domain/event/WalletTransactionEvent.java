package br.me.vitorcsouza.payflow.transaction.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WalletTransactionEvent(
        UUID eventId,
        UUID walletId,
        BigDecimal amount,
        String type,
        String description,
        LocalDateTime createdAt
) {
}
