package br.me.vitorcsouza.payflow.transaction.domain.service.impl;

import br.me.vitorcsouza.payflow.transaction.domain.dto.request.CreateTransactionRequestDTO;
import br.me.vitorcsouza.payflow.transaction.domain.dto.response.TransactionResponseDTO;
import br.me.vitorcsouza.payflow.transaction.domain.model.Transaction;
import br.me.vitorcsouza.payflow.transaction.domain.repository.TransactionRepository;
import br.me.vitorcsouza.payflow.transaction.domain.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;


    @Override
    @Transactional
    public TransactionResponseDTO createTransaction(CreateTransactionRequestDTO request) {
        Transaction transaction = request.toEntity();
        transactionRepository.save(transaction);
        return TransactionResponseDTO.fromEntity(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionsByWalletId(UUID walletId) {
        return transactionRepository.findByWalletId(walletId)
                .stream()
                .map(TransactionResponseDTO::fromEntity)
                .toList();
    }
}
