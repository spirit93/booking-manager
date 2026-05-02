package com.vits.booking.booking;

import java.time.Instant;
import java.util.UUID;

public record BookingResponse(UUID id, UUID seatId, UUID customerId, BookingStatus status, Instant createdAt) {

    static BookingResponse from(BookingEntity booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getSeat().getId(),
                booking.getCustomerId(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }
}
