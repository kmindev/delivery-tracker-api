package com.example.service;

import com.example.domain.constant.Company;
import com.example.dto.DeliveryStatus;
import com.example.dto.delivery_tracker.request.DeliveryTrackerCallbackRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeliveryCallbackWorker {

    private final Queue<DeliveryTrackerCallbackRequest> deliveryCallbackQueue;
    private final DeliveryTrackerService deliveryTrackerService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 1000) // 1초마다 실행
    public void processQueue() {
        while (!deliveryCallbackQueue.isEmpty()) {
            DeliveryTrackerCallbackRequest request = deliveryCallbackQueue.poll();
            Company company = Company.fromId(request.carrierId());
            String trackingNumber = request.trackingNumber();
            DeliveryStatus deliveryStatus = deliveryTrackerService.getAllEvents(company, trackingNumber);

            // TODO: 배송 상태 DB 업데이트
            try {
                String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(deliveryStatus);
                log.info("배송 상태 업데이트 - {}/{}\n{}", company, trackingNumber, json);
            } catch (JsonProcessingException e) {
                log.warn("배송 상태 JSON 직렬화 실패 - {}/{}: {}", company, trackingNumber, e.getMessage());
                log.info("배송 상태: {}", deliveryStatus);
            }
        }
    }

}
