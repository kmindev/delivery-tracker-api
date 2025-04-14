package com.example.dto.delivery_tracker.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DeliveryTrackerCarriersResponse(List<Error> errors, Data data) {

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

    public record Error(
            String message,
            List<Location> locations,
            List<String> path,
            Extensions extensions
    ) {
        public record Location(Integer line, Integer column) {
        }

        public record Extensions(
                // UNAUTHENTICATED
                // FORBIDDEN
                // INTERNAL
                // BAD_REQUEST
                // NOT_FOUND
                String code
        ) {
        }
    }

}
