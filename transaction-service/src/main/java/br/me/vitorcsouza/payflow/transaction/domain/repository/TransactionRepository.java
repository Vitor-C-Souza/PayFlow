package br.me.vitorcsouza.payflow.transaction.domain.repository;

import br.me.vitorcsouza.payflow.transaction.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByWalletId(UUID walletId);

    boolean existsByEventId(UUID eventId);
}
