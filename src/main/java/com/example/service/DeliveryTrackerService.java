package com.example.service;

import com.example.dto.delivery_tracker.request.DeliveryTrackerRequest;
import com.example.dto.delivery_tracker.response.DeliveryTrackerCarriersResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class DeliveryTrackerService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public DeliveryTrackerCarriersResponse searchCarriers(String searchText) throws JsonProcessingException {
        String query = QueryFactory.searchCarriesQuery();
        Map<String, Object> variables = Map.of(
                "searchText", searchText
        );
        DeliveryTrackerRequest requestBody = new DeliveryTrackerRequest(query, variables);

        // API 호출
        String responseBody = restClient.post()
                .body(requestBody)
                .retrieve()
                .body(String.class);
        System.out.println("responseBody = " + responseBody);
        return objectMapper.readValue(responseBody, DeliveryTrackerCarriersResponse.class);
    }

}

