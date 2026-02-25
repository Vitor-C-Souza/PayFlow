package br.me.vitorcsouza.payflow.antifraud_service.domain.events;

import java.util.UUID;

public record TransactionApprovedEvent(
        UUID transactionId
) {
}
