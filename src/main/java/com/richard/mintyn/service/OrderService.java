package com.richard.mintyn.service;

import com.richard.mintyn.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    public void publishOrder(Order order) {
        kafkaTemplate.send("orders", order);
    }
}
