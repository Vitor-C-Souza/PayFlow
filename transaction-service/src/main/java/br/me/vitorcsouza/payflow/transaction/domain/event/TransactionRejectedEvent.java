package br.me.vitorcsouza.payflow.transaction.domain.event;

import java.util.UUID;

public record TransactionRejectedEvent(
        UUID transactionId,
        String reason
) {
}
