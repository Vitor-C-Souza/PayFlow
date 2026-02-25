package br.me.vitorcsouza.payflow.antifraud_service.domain.service.impl;

import br.me.vitorcsouza.payflow.antifraud_service.domain.events.TransactionCreatedEvent;
import br.me.vitorcsouza.payflow.antifraud_service.domain.service.AntifraudService;
import br.me.vitorcsouza.payflow.antifraud_service.infra.messaging.AntifraudPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AntifraudServiceImpl implements AntifraudService {

    private final AntifraudPublisher publisher;

    @Override
    public void analyze(TransactionCreatedEvent event) {
        if(event.amount().compareTo(new BigDecimal("10000"))>0){
            publisher.reject(event.transactionId(), "Amount to high");
            return;
        }
        publisher.approve(event.transactionId());
    }
}
