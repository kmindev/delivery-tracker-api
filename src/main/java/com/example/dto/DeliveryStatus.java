package com.example.dto;

import com.example.domain.constant.Company;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@ToString
@Getter
@Builder
public class DeliveryStatus {

    private String trackingNumber;
    private Company company;
    private Event lastEvent;
    private List<Event> events;

    // 에러
    private String errorMessage;
    private String errorCode;

    @ToString
    @Getter
    @Builder
    public static class Event {
        private String code;
        private String name;
        private String description;
        private LocalDateTime time;
    }

}
