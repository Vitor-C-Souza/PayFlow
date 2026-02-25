package br.me.vitorcsouza.payflow.antifraud_service.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String TRANSACTION_EXCHANGE = "transaction.exchange";

    public static final String TRANSACTION_CREATED_QUEUE = "transaction.created";
    public static final String TRANSACTION_APPROVED_QUEUE = "transaction.approved";
    public static final String TRANSACTION_REJECTED_QUEUE = "transaction.rejected";

    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange(TRANSACTION_EXCHANGE);
    }

    @Bean
    public Queue createdQueue() {
        return new Queue(TRANSACTION_CREATED_QUEUE);
    }

    @Bean
    public Queue approvedQueue() {
        return new Queue(TRANSACTION_APPROVED_QUEUE);
    }

    @Bean
    public Queue rejectedQueue() {
        return new Queue(TRANSACTION_REJECTED_QUEUE);
    }

    @Bean
    public Binding createdBinding(Queue createdQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(createdQueue)
                .to(exchange)
                .with("transaction.created");
    }

    @Bean
    public Binding approvedBinding(Queue approvedQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(approvedQueue)
                .to(exchange)
                .with("transaction.approved");
    }

    @Bean
    public Binding rejectedBinding(Queue rejectedQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(rejectedQueue)
                .to(exchange)
                .with("transaction.rejected");
    }
}
