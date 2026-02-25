package br.me.vitorcsouza.payflow.antifraud_service.domain.service;

import br.me.vitorcsouza.payflow.antifraud_service.domain.events.TransactionCreatedEvent;

public interface AntifraudService {
    void analyze(TransactionCreatedEvent event);
}
