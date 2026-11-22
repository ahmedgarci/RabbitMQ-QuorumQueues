
package com.example.demo;

import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

@Service
public class OrderConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleOrder(
            Message message,
            Channel channel
    ) throws Exception {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        String body = new String(message.getBody(),StandardCharsets.UTF_8);

        boolean redelivered = message.getMessageProperties().isRedelivered();
        System.out.println("Received: " + body);
        System.out.println("Redelivered: " + redelivered);
        System.out.println("Delivery tag: " + deliveryTag);
        System.out.println("Redelivered: " + redelivered);
        if (body.contains("FAILS") ) {
            throw new RuntimeException("Order processing failed!");
        }
        System.out.println("ACK: Message processed successfully");
        channel.basicAck(deliveryTag, false);
    }
}
