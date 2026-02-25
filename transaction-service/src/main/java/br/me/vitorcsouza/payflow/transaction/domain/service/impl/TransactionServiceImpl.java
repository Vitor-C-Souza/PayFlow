package br.me.vitorcsouza.payflow.transaction.domain.service.impl;

import br.me.vitorcsouza.payflow.transaction.domain.dto.request.CreateTransactionRequestDTO;
import br.me.vitorcsouza.payflow.transaction.domain.dto.response.TransactionResponseDTO;
import br.me.vitorcsouza.payflow.transaction.domain.event.TransactionCreatedEvent;
import br.me.vitorcsouza.payflow.transaction.domain.model.Transaction;
import br.me.vitorcsouza.payflow.transaction.domain.model.TransactionStatus;
import br.me.vitorcsouza.payflow.transaction.domain.repository.TransactionRepository;
import br.me.vitorcsouza.payflow.transaction.domain.service.TransactionService;
import br.me.vitorcsouza.payflow.transaction.infra.messaging.TransactionProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProducer transactionProducer;


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

    @Override
    @Transactional
    public void approveTransaction(UUID transactionId) {
        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow();

        transaction.setStatus(TransactionStatus.ACCEPTED);

        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void rejectTransaction(UUID transactionId, String reason) {
        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow();

        transaction.setStatus(TransactionStatus.REJECTED);

        transaction.setFailureReason(reason);

        transactionRepository.save(transaction);
    }

    @Override
    public void publishTransactionCreated(TransactionCreatedEvent event) {
       transactionProducer.publishTransactionCreated(event);
    }
}
