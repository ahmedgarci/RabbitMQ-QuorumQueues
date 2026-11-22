package com.example.demo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "orders.quorum.queue";

    public static final String DLX = "orders.dlx";
    public static final String DLQ = "orders.dlq";
    public static final String DLQ_ROUTING_KEY = "orders.failed";

    @Bean
    public Queue ordersQuorumQueue() {
        return QueueBuilder.durable(QUEUE)
                .quorum()
                .deadLetterExchange(DLX)
                .deadLetterRoutingKey(DLQ_ROUTING_KEY)
                .build();
    }

    @Bean("dlq_queue")
    public Queue dlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean("dlq_exchange")
    public DirectExchange dlqExchange() {
        return new DirectExchange(DLX);
    }

    @Bean
    public Binding dlqBinding(
            @Qualifier("dlq_queue") Queue queue,
            @Qualifier("dlq_exchange") DirectExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(DLQ_ROUTING_KEY);

    }
}