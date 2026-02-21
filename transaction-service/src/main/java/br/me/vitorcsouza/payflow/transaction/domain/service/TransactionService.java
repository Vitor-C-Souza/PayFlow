package br.me.vitorcsouza.payflow.transaction.domain.service;

import br.me.vitorcsouza.payflow.transaction.domain.dto.request.CreateTransactionRequestDTO;
import br.me.vitorcsouza.payflow.transaction.domain.dto.response.TransactionResponseDTO;

import java.util.UUID;
import java.util.List;

public interface TransactionService {
    TransactionResponseDTO createTransaction(CreateTransactionRequestDTO request);

    List<TransactionResponseDTO> getTransactionsByWalletId(UUID walletId);
}
