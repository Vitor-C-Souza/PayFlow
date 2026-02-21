package br.me.vitorcsouza.payflow.transaction.domain.dto.request;

import br.me.vitorcsouza.payflow.transaction.domain.model.Transaction;
import br.me.vitorcsouza.payflow.transaction.domain.model.TransactionType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionRequestDTO(
        @NotNull UUID walletId,
        @NotNull BigDecimal amount,
        @NotNull TransactionType type,
        String description
) {
    public Transaction toEntity() {
        return Transaction.builder()
                .walletId(this.walletId)
                .amount(this.amount)
                .type(this.type)
                .description(this.description)
                .build();
    }
}
