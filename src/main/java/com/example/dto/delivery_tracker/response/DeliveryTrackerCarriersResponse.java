package com.example.dto.delivery_tracker.response;

import java.util.List;

public record DeliveryTrackerCarriersResponse(Data data) {

    public record Data(Carriers carriers) {
    }

    public record Carriers(PageInfo pageInfo, List<Edge> edges) {
    }

    public record PageInfo(Boolean hasNextPage, String endCursor) {
    }

    public record Edge(Node node) {
        public record Node(String id, String name) {
        }
    }

}
