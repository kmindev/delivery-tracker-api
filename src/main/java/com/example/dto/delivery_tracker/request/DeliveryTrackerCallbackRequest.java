package com.example.dto.delivery_tracker.request;

public record DeliveryTrackerCallbackRequest(
        String carrierId,
        String trackingNumber
) {
}
