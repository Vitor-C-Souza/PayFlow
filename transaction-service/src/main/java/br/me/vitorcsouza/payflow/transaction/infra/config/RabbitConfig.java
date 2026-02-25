package br.me.vitorcsouza.payflow.transaction.infra.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "wallet.events";
    public static final String QUEUE = "transaction.queue";
    public static final String ROUTING_KEY = "wallet.transaction";

    public static final String DLQ = "transaction.dlq";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder
                .durable(QUEUE)
                .withArgument("x-dead-letter-exchange", EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "wallet.transaction.dlq")
                .build();
    }

    @Bean
    public Queue dlq() {
        return new Queue(DLQ);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding(Queue dlq, TopicExchange exchange) {
        return BindingBuilder
                .bind(dlq)
                .to(exchange)
                .with("wallet.transaction.dlq");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static final String TRANSACTION_APPROVED_QUEUE = "transaction.approved";
    public static final String TRANSACTION_REJECTED_QUEUE = "transaction.rejected";

    @Bean
    public Queue approvedQueue() {
        return new Queue(TRANSACTION_APPROVED_QUEUE);
    }

    @Bean
    public Queue rejectedQueue() {
        return new Queue(TRANSACTION_REJECTED_QUEUE);
    }
}
