package com.example.dto.delivery_tracker.response;

import com.example.domain.constant.Company;
import com.example.dto.DeliveryStatus;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record DeliveryTrackerTrackResponse(List<Error> errors, Data data) {

    public record Data(Track track) {

        public record Track(Node lastEvent, Events events) {

            public record Events(List<Edge> edges) {

                public record Edge(Node node) {

                    public Status status() {
                        return Optional.ofNullable(node).map(Node::status).orElse(null);
                    }

                    public String code() {
                        return Optional.ofNullable(status()).map(Status::code).orElse(null);
                    }

                    public String name() {
                        return Optional.ofNullable(status()).map(Status::name).orElse(null);
                    }

                    public String description() {
                        return Optional.ofNullable(node).map(Node::description).orElse(null);
                    }

                    public ZonedDateTime time() {
                        return Optional.ofNullable(node).map(Node::time).orElse(null);
                    }

                    public DeliveryStatus.Event toEvent() {
                        return DeliveryStatus.Event.builder()
                                .code(code())
                                .name(name())
                                .description(description())
                                .time(Optional.ofNullable(time()).map(ZonedDateTime::toLocalDateTime).orElse(null))
                                .build();
                    }
                }
            }
        }
    }

    public record Node(ZonedDateTime time, Status status, String description) {
    }

    public record Status(String code, String name) {
    }

    public String errorMessage() {
        return Optional.ofNullable(errors)
                .filter(e -> !e.isEmpty())
                .map(e -> e.get(0).message())
                .orElse(null);
    }

    public String errorCode() {
        return Optional.ofNullable(errors)
                .filter(e -> !e.isEmpty())
                .map(e -> e.get(0).extensions().code())
                .orElse(null);
    }

    public Data.Track.Events events() {
        return Optional.ofNullable(data)
                .map(Data::track)
                .map(Data.Track::events)
                .orElse(null);
    }

    public List<Data.Track.Events.Edge> edges() {
        return Optional.ofNullable(events())
                .map(Data.Track.Events::edges)
                .orElse(Collections.emptyList());
    }

    public Data.Track track() {
        return Optional.ofNullable(data).map(Data::track).orElse(null);
    }

    public Node lastEvent() {
        return Optional.ofNullable(track()).map(Data.Track::lastEvent).orElse(null);
    }

    public Status lastEventStatus() {
        return Optional.ofNullable(lastEvent()).map(Node::status).orElse(null);
    }

    public String lastEventCode() {
        return Optional.ofNullable(lastEventStatus()).map(Status::code).orElse(null);
    }

    public String lastEventName() {
        return Optional.ofNullable(lastEventStatus()).map(Status::name).orElse(null);
    }

    public String lastEventDescription() {
        return Optional.ofNullable(lastEvent()).map(Node::description).orElse(null);
    }

    public ZonedDateTime lastEventTime() {
        return Optional.ofNullable(lastEvent()).map(Node::time).orElse(null);
    }

    public DeliveryStatus of(Company company, String trackingNumber) {
        if (errors != null) {
            return DeliveryStatus.builder()
                    .trackingNumber(trackingNumber)
                    .company(company)
                    .errorMessage(errorMessage())
                    .errorCode(errorCode())
                    .build();
        }

        DeliveryStatus.Event lastEvent = DeliveryStatus.Event.builder()
                .code(lastEventCode())
                .name(lastEventName())
                .description(lastEventDescription())
                .time(Optional.ofNullable(lastEventTime()).map(ZonedDateTime::toLocalDateTime).orElse(null))
                .build();

        List<DeliveryStatus.Event> events = edges().stream()
                .map(Data.Track.Events.Edge::toEvent)
                .sorted(Comparator.comparing(DeliveryStatus.Event::getTime,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        return DeliveryStatus.builder()
                .trackingNumber(trackingNumber)
                .company(company)
                .lastEvent(lastEvent)
                .events(events)
                .build();
    }

}
