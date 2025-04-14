package com.example.dto.delivery_tracker.response;

import java.time.ZonedDateTime;
import java.util.List;

public record DeliveryTrackerTrackResponse(List<Error> errors, Data data) {

    public record Data(Track track) {

        public record Track(Node lastEvent, Events events) {

            public record Events(List<Edge> edges) {

                public record Edge(Node node) {

                }

            }

        }
    }

    public record Node(ZonedDateTime time, Status status, String description) {
    }

    public record Status(String code, String name) {
    }

}
