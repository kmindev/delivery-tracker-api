package com.example.controller;

import com.example.domain.constant.Company;
import com.example.dto.delivery_tracker.request.DeliveryTrackerCallbackRequest;
import com.example.dto.delivery_tracker.response.DeliveryTrackerCarriersResponse;
import com.example.dto.delivery_tracker.response.DeliveryTrackerTrackResponse;
import com.example.dto.delivery_tracker.response.DeliveryWebhookRegisterResponse;
import com.example.service.DeliveryTrackerService;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/delivery-tracker")
@RestController
public class DeliveryTrackerController {

    private final DeliveryTrackerService deliveryTrackerService;
    private final Queue<DeliveryTrackerCallbackRequest> deliveryCallbackQueue;

    @GetMapping("/carriers")
    public DeliveryTrackerCarriersResponse getCarriers(@RequestParam String searchText) {
        return deliveryTrackerService.searchCarriers(searchText);
    }

    @GetMapping("/last-event")
    public DeliveryTrackerTrackResponse getLastEvent(
            @RequestParam Company company,
            @RequestParam String trackingNumber
    ) {
        return deliveryTrackerService.getLastEvent(company, trackingNumber);
    }

    @GetMapping("/all-events")
    public DeliveryTrackerTrackResponse getAllEvents(
            @RequestParam Company company,
            @RequestParam String trackingNumber
    ) {
        return deliveryTrackerService.getAllEvents(company, trackingNumber);
    }

    @GetMapping("/register")
    public DeliveryWebhookRegisterResponse register(
            @RequestParam Company company,
            @RequestParam String trackingNumber
    ) {
        return deliveryTrackerService.register(company, trackingNumber);
    }

    @PostMapping("/callback")
    public ResponseEntity<Void> callback(@RequestBody DeliveryTrackerCallbackRequest request) {
        deliveryCallbackQueue.offer(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
