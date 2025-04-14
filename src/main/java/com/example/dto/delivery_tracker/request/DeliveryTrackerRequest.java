package com.example.dto.delivery_tracker.request;

import java.util.Map;

public record DeliveryTrackerRequest(
        String query,
        Map<String, Object> variables
) {
}
