package com.example.controller;

import com.example.domain.constant.Company;
import com.example.dto.DeliveryStatus;
import com.example.dto.delivery_tracker.request.DeliveryTrackerCallbackRequest;
import com.example.dto.delivery_tracker.response.DeliveryTrackerCarriersResponse;
import com.example.dto.delivery_tracker.response.DeliveryTrackerTrackResponse;
import com.example.dto.delivery_tracker.response.DeliveryWebhookRegisterResponse;
import com.example.service.DeliveryTrackerService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/delivery-tracker")
@RestController
public class DeliveryTrackerController extends BaseController {

    private final DeliveryTrackerService deliveryTrackerService;
    private final Queue<DeliveryTrackerCallbackRequest> deliveryCallbackQueue;

    @GetMapping("/carriers")
    public ResponseEntity<DeliveryTrackerCarriersResponse> getCarriers(
            @RequestParam String searchText,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest, searchText);
        DeliveryTrackerCarriersResponse response = deliveryTrackerService.searchCarriers(searchText);
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/last-event")
    public ResponseEntity<DeliveryStatus> getLastEvent(
            @RequestParam Company company,
            @RequestParam String trackingNumber,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest);
        DeliveryStatus response = deliveryTrackerService.getLastEvent(company, trackingNumber);
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-events")
    public ResponseEntity<DeliveryStatus> getAllEvents(
            @RequestParam Company company,
            @RequestParam String trackingNumber,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest);
        DeliveryStatus response = deliveryTrackerService.getAllEvents(company, trackingNumber);
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/register")
    public ResponseEntity<DeliveryWebhookRegisterResponse> register(
            @RequestParam Company company,
            @RequestParam String trackingNumber,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest);
        DeliveryWebhookRegisterResponse response = deliveryTrackerService.register(company, trackingNumber);
        responseLog(log, httpServletRequest, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/callback")
    public ResponseEntity<Void> callback(
            @RequestBody DeliveryTrackerCallbackRequest request,
            HttpServletRequest httpServletRequest
    ) {
        requestLog(log, httpServletRequest, request);
        deliveryCallbackQueue.offer(request);
        responseLog(log, httpServletRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
