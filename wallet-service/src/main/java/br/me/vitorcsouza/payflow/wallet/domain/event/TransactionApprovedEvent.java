package br.me.vitorcsouza.payflow.wallet.domain.event;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionApprovedEvent(
        UUID transactionId,
        UUID walletId,
        String type,
        BigDecimal amount
) {
}
