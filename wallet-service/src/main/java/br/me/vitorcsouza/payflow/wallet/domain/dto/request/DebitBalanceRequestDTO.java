package br.me.vitorcsouza.payflow.wallet.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DebitBalanceRequestDTO(
        @NotNull
        @Positive
        BigDecimal amount
) {
}
