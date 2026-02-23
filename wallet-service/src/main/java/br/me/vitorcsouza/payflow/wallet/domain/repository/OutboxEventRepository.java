package br.me.vitorcsouza.payflow.wallet.domain.repository;


import java.util.UUID;

import br.me.vitorcsouza.payflow.wallet.domain.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByProcessedFalse();
}
