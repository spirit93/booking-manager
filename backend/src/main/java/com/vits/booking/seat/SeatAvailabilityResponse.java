package com.vits.booking.seat;

import java.time.LocalDate;
import java.util.List;

public record SeatAvailabilityResponse(LocalDate day, List<SeatAvailabilityItem> seats) {
    public record SeatAvailabilityItem(String seatId, String label, String status, String bookingId) {
    }
}
