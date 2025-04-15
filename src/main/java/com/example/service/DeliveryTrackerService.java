package com.example.service;

import com.example.domain.constant.Company;
import com.example.dto.delivery_tracker.request.DeliveryTrackerRequest;
import com.example.dto.delivery_tracker.response.DeliveryTrackerCarriersResponse;
import com.example.dto.delivery_tracker.response.DeliveryTrackerTrackResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class DeliveryTrackerService {

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

}

