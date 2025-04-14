package com.example.dto.delivery_tracker.response;

import java.util.List;

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
