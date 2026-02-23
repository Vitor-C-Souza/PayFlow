package br.me.vitorcsouza.payflow.wallet.domain.service.impl;

import br.me.vitorcsouza.payflow.wallet.domain.dto.request.CreateWalletRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.request.CreditBalanceRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.request.DebitBalanceRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.response.WalletResponseDTO;
import br.me.vitorcsouza.payflow.wallet.domain.model.Wallet;
import br.me.vitorcsouza.payflow.wallet.domain.repository.WalletRepository;
import br.me.vitorcsouza.payflow.wallet.domain.service.WalletService;
import br.me.vitorcsouza.payflow.wallet.domain.event.WalletEventPublisher;
import br.me.vitorcsouza.payflow.wallet.domain.event.WalletTransactionEvent;
import br.me.vitorcsouza.payflow.wallet.infra.exception.WalletAlreadyExistsException;
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
    private final WalletEventPublisher walletEventPublisher;


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

        WalletTransactionEvent credit = new WalletTransactionEvent(
                UUID.randomUUID(),
                wallet.getId(),
                request.amount(),
                "CREDIT",
                "CREDIT: +" + request.amount(),
                LocalDateTime.now()
        );
        walletEventPublisher.publishTransaction(credit);

        return WalletResponseDTO.fromEntity(wallet);
    }

    @Override
    @Transactional
    public WalletResponseDTO debit(UUID walletId, DebitBalanceRequestDTO request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        wallet.debit(request.amount());

        walletRepository.save(wallet);

        WalletTransactionEvent debit = new WalletTransactionEvent(
                UUID.randomUUID(),
                wallet.getId(),
                request.amount(),
                "DEBIT",
                "DEBIT: -" + request.amount(),
                LocalDateTime.now()
        );
        walletEventPublisher.publishTransaction(debit);

        return WalletResponseDTO.fromEntity(wallet);
    }
}
