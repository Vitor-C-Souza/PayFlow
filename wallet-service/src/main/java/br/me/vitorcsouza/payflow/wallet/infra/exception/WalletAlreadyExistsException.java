package br.me.vitorcsouza.payflow.wallet.infra.exception;

public class WalletAlreadyExistsException extends RuntimeException {
    public WalletAlreadyExistsException() {
        super("Wallet already exists for this user");
    }
}
