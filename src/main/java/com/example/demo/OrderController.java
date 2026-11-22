package com.example.demo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
public class OrderController {

     private final RabbitTemplate rabbitTemplate;

    public OrderController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/orders/{message}")
    public String send(@PathVariable String message) {

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.QUEUE,
            message
        );

        return "Message sent: " + message;
    }
}
