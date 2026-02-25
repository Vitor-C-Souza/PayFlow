package br.me.vitorcsouza.payflow.wallet.domain.service;

import br.me.vitorcsouza.payflow.wallet.domain.dto.request.CreateWalletRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.request.CreditBalanceRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.request.DebitBalanceRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.response.WalletResponseDTO;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {
    WalletResponseDTO createWallet(CreateWalletRequestDTO request);

    WalletResponseDTO getWalletByUserId(UUID userId);

    WalletResponseDTO credit(UUID walletId, CreditBalanceRequestDTO request);

    WalletResponseDTO debit(UUID walletId, DebitBalanceRequestDTO request);

    void applyTransaction(UUID transactionId, String type, BigDecimal amount);
}
