package br.me.vitorcsouza.payflow.wallet.domain.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record WalletTransactionEvent (
        UUID walletId,
        BigDecimal amount,
        String type) {}
