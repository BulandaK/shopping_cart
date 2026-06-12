package com.example.shopping_cart.producer;

import com.example.shopping_cart.dto.CartCheckoutEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartEventProducer {

    private final KafkaTemplate<String, CartCheckoutEvent> kafkaTemplate;
    private static final String TOPIC = "cart-checkout-topic";

    public void sendCheckoutEvent(CartCheckoutEvent event) {
        log.info("Sending event checkout for user: {} on topic: {}", event.userId(),TOPIC);
        kafkaTemplate.send(TOPIC,event.userId().toString(), event);
    }
}
