package com.example.service;

import com.example.domain.constant.Company;
import com.example.dto.delivery_tracker.request.DeliveryTrackerCallbackRequest;
import com.example.dto.delivery_tracker.response.DeliveryTrackerTrackResponse;
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

    @Scheduled(fixedDelay = 1000) // 1초마다 실행
    public void processQueue() {
        while (!deliveryCallbackQueue.isEmpty()) {
            DeliveryTrackerCallbackRequest request = deliveryCallbackQueue.poll();
            Company company = Company.fromId(request.carrierId());
            String trackingNumber = request.trackingNumber();
            DeliveryTrackerTrackResponse allEvents = deliveryTrackerService.getAllEvents(company, trackingNumber);

            // TODO: 배송 상태 DB 업데이트
            log.info("배송 상태 업데이트: {}/{}\n{}", company, trackingNumber, allEvents);
        }
    }

}
