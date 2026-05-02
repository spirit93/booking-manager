package com.vits.booking.booking;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record BookingResponse(UUID id, UUID seatId, String customerEmail, LocalDate bookedDay, BookingStatus status, Instant createdAt) {

    static BookingResponse from(BookingEntity booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getSeat().getId(),
                booking.getCustomerEmail(),
                booking.getBookedDay(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }
}
