package br.me.vitorcsouza.payflow.wallet.domain.event;

public interface WalletEventPublisher {

    void publishTransaction(WalletTransactionEvent event);
}
