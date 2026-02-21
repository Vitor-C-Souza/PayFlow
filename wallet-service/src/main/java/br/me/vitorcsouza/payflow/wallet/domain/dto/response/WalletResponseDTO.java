package br.me.vitorcsouza.payflow.wallet.domain.dto.response;

import br.me.vitorcsouza.payflow.wallet.domain.model.Wallet;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponseDTO(
        UUID id,
        UUID userId,
        BigDecimal balance
) {
    public static WalletResponseDTO fromEntity(Wallet wallet) {
        return new WalletResponseDTO(
                wallet.getId(),
                wallet.getUserId(),
                wallet.getBalance()
        );
    }
}
