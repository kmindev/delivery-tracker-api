package com.example.dto.delivery_tracker.response;

import java.util.List;

public record DeliveryWebhookRegisterResponse(List<Error> errors, Data data) {

    public record Data(Boolean registerTrackWebhook) {

    }

}
