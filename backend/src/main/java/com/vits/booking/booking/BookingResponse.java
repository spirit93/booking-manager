package com.vits.booking.booking;

import java.time.Instant;
import java.time.LocalDate;

public record BookingResponse(String bookingId, String seatId, LocalDate day, String status, Instant createdAt) {
    static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getSeat().getId(),
                booking.getBookingDay(),
                booking.getStatus().name(),
                booking.getCreatedAt());
    }
}
