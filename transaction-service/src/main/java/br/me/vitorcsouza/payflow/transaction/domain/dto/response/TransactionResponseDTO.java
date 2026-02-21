package br.me.vitorcsouza.payflow.transaction.domain.dto.response;

import br.me.vitorcsouza.payflow.transaction.domain.model.Transaction;
import br.me.vitorcsouza.payflow.transaction.domain.model.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionResponseDTO(
        UUID id,
        UUID walletId,
        BigDecimal amount,
        TransactionType type,
        String description
) {

    public static TransactionResponseDTO fromEntity(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getWalletId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription()
        );
     }
}
