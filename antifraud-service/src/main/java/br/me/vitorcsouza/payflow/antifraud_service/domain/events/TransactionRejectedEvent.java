package br.me.vitorcsouza.payflow.antifraud_service.domain.events;

import java.util.UUID;

public record TransactionRejectedEvent(
        UUID transactionId,
        String reason
) {
}
