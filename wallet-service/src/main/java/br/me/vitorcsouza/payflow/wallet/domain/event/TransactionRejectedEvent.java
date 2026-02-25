package br.me.vitorcsouza.payflow.wallet.domain.event;

import java.util.UUID;

public record TransactionRejectedEvent(
        UUID transactionId,
        String reason
) {
}
