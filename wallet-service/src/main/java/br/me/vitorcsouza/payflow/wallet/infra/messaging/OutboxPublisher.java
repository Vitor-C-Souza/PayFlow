package br.me.vitorcsouza.payflow.wallet.infra.messaging;

import br.me.vitorcsouza.payflow.wallet.domain.event.WalletTransactionEvent;
import br.me.vitorcsouza.payflow.wallet.domain.model.OutboxEvent;
import br.me.vitorcsouza.payflow.wallet.domain.repository.OutboxEventRepository;
import br.me.vitorcsouza.payflow.wallet.infra.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher  {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {

        List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();

        for (OutboxEvent event : events) {

            try {


                WalletTransactionEvent walletTransactionEvent = objectMapper.readValue(event.getPayload(), WalletTransactionEvent.class);
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE,
                        RabbitMQConfig.ROUTING_KEY,
                        walletTransactionEvent
                );

                event.setProcessed(true);
                event.setProcessedAt(LocalDateTime.now());
                outboxEventRepository.save(event);

                log.info("Event published {}", event.getId());

            } catch (Exception e) {

                log.error("Error publishing event {}", event.getId(), e);

            }

        }
    }
}
