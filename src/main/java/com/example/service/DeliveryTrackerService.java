package com.example.service;

import com.example.domain.constant.Company;
import com.example.dto.delivery_tracker.request.DeliveryTrackerRequest;
import com.example.dto.delivery_tracker.response.DeliveryTrackerCarriersResponse;
import com.example.dto.delivery_tracker.response.DeliveryTrackerTrackResponse;
import com.example.dto.delivery_tracker.response.DeliveryWebhookRegisterResponse;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class DeliveryTrackerService {

    @Value("${delivery-tracker.call-back-url}")
    private String callbackUrl;

    private final RestClient restClient;

    public DeliveryTrackerCarriersResponse searchCarriers(String searchText) {
        String query = QueryFactory.searchCarriesQuery();
        Map<String, Object> variables = Map.of(
                "searchText", searchText
        );
        DeliveryTrackerRequest requestBody = new DeliveryTrackerRequest(query, variables);
        return restClient.post()
                .body(requestBody)
                .retrieve()
                .body(DeliveryTrackerCarriersResponse.class);
    }

    public DeliveryTrackerTrackResponse getLastEvent(Company company, String trackingNumber) {
        String query = QueryFactory.lastEventQuery();
        Map<String, Object> variables = Map.of(
                "carrierId", company.getId(),
                "trackingNumber", trackingNumber
        );
        DeliveryTrackerRequest requestBody = new DeliveryTrackerRequest(query, variables);
        return restClient.post()
                .body(requestBody)
                .retrieve()
                .body(DeliveryTrackerTrackResponse.class);
    }

    public DeliveryTrackerTrackResponse getAllEvents(Company company, String trackingNumber) {
        String query = QueryFactory.allEventsQuery();
        Map<String, Object> variables = Map.of(
                "carrierId", company.getId(),
                "trackingNumber", trackingNumber
        );
        DeliveryTrackerRequest requestBody = new DeliveryTrackerRequest(query, variables);
        return restClient.post()
                .body(requestBody)
                .retrieve()
                .body(DeliveryTrackerTrackResponse.class);
    }

    public DeliveryWebhookRegisterResponse register(Company company, String trackingNumber) {
        String query = QueryFactory.webhookRegisterQuery();
        String expirationTime = ZonedDateTime.now(ZoneOffset.UTC)
                .plusDays(2)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        Map<String, Object> variables = Map.of(
                "carrierId", company.getId(),
                "trackingNumber", trackingNumber,
                "callbackUrl", callbackUrl,
                "expirationTime", expirationTime
        );
        Map<String, Object> input = Map.of("input", variables);
        DeliveryTrackerRequest requestBody = new DeliveryTrackerRequest(query, input);
        return restClient.post()
                .body(requestBody)
                .retrieve()
                .body(DeliveryWebhookRegisterResponse.class);
    }

}

