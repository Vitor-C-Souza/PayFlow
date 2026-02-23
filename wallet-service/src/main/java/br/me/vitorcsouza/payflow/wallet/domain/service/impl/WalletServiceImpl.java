package br.me.vitorcsouza.payflow.wallet.domain.service.impl;

import br.me.vitorcsouza.payflow.wallet.domain.dto.request.CreateWalletRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.request.CreditBalanceRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.request.DebitBalanceRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.response.WalletResponseDTO;
import br.me.vitorcsouza.payflow.wallet.domain.model.OutboxEvent;
import br.me.vitorcsouza.payflow.wallet.domain.model.Wallet;
import br.me.vitorcsouza.payflow.wallet.domain.repository.OutboxEventRepository;
import br.me.vitorcsouza.payflow.wallet.domain.repository.WalletRepository;
import br.me.vitorcsouza.payflow.wallet.domain.service.WalletService;
import br.me.vitorcsouza.payflow.wallet.domain.event.WalletTransactionEvent;
import br.me.vitorcsouza.payflow.wallet.infra.exception.WalletAlreadyExistsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;


    @Override
    @Transactional
    public WalletResponseDTO createWallet(CreateWalletRequestDTO request) {
        walletRepository.findByUserId(request.userId())
                .ifPresent(w -> {
                    throw new WalletAlreadyExistsException();
                });

        Wallet wallet = Wallet.builder()
                .userId(request.userId())
                .build();

        return WalletResponseDTO.fromEntity(walletRepository.save(wallet));
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponseDTO getWalletByUserId(UUID userId) {
        return WalletResponseDTO.fromEntity(walletRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found")));

    }

    @Override
    @Transactional
    public WalletResponseDTO credit(UUID walletId, CreditBalanceRequestDTO request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        wallet.credit(request.amount());

        walletRepository.save(wallet);

        WalletTransactionEvent event = new WalletTransactionEvent(
                UUID.randomUUID(),
                wallet.getId(),
                request.amount(),
                "CREDIT",
                "CREDIT: +" + request.amount(),
                LocalDateTime.now()
        );

        saveOutboxEvent(event);

        return WalletResponseDTO.fromEntity(wallet);
    }



    @Override
    @Transactional
    public WalletResponseDTO debit(UUID walletId, DebitBalanceRequestDTO request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        wallet.debit(request.amount());

        walletRepository.save(wallet);

        WalletTransactionEvent event = new WalletTransactionEvent(
                UUID.randomUUID(),
                wallet.getId(),
                request.amount(),
                "DEBIT",
                "DEBIT: -" + request.amount(),
                LocalDateTime.now()
        );

        saveOutboxEvent(event);

        return WalletResponseDTO.fromEntity(wallet);
    }

    private void saveOutboxEvent(WalletTransactionEvent event) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateType("WALLET")
                .aggregateId(event.walletId())
                .eventType(event.getClass().getSimpleName())
                .payload(payload)
                .processed(false)
                .createdAt(LocalDateTime.now())
                .build();

        outboxEventRepository.save(outboxEvent);
    }
}
