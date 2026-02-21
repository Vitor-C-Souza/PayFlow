package br.me.vitorcsouza.payflow.wallet.domain.repository;

import br.me.vitorcsouza.payflow.wallet.domain.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByUserId(UUID userId);
}
