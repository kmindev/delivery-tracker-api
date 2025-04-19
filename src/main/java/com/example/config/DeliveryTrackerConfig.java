package com.example.config;

import com.example.dto.delivery_tracker.request.DeliveryTrackerCallbackRequest;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliveryTrackerConfig {

    @Bean
    public Queue<DeliveryTrackerCallbackRequest> deliveryCallbackQueue() {
        return new ConcurrentLinkedQueue<>();
    }

}
