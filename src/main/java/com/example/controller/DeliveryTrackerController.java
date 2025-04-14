package com.example.controller;

import com.example.dto.delivery_tracker.response.DeliveryTrackerCarriersResponse;
import com.example.service.DeliveryTrackerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/delivery-tracker")
@RestController
public class DeliveryTrackerController {

    private final DeliveryTrackerService deliveryTrackerService;

    @GetMapping("/carriers")
    public DeliveryTrackerCarriersResponse getCarriers(@RequestParam String searchText) {
        return deliveryTrackerService.searchCarriers(searchText);
    }

}
