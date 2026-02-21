package br.me.vitorcsouza.payflow.wallet.domain.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWalletRequestDTO(
        @NotNull
        UUID userId
) {
}
