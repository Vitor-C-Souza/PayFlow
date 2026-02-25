package br.me.vitorcsouza.payflow.transaction.domain.service;

import br.me.vitorcsouza.payflow.transaction.domain.dto.request.CreateTransactionRequestDTO;
import br.me.vitorcsouza.payflow.transaction.domain.dto.response.TransactionResponseDTO;
import br.me.vitorcsouza.payflow.transaction.domain.event.TransactionCreatedEvent;

import java.util.UUID;
import java.util.List;

public interface TransactionService {
    TransactionResponseDTO createTransaction(CreateTransactionRequestDTO request);

    List<TransactionResponseDTO> getTransactionsByWalletId(UUID walletId);

    void approveTransaction(UUID transactionId);

    void rejectTransaction(UUID transactionId, String reason);

    void publishTransactionCreated(TransactionCreatedEvent event);
}
