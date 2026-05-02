package com.vits.booking.seat;

import java.time.LocalDate;
import java.util.List;

public record SeatAvailabilityResponse(LocalDate day, List<SeatResponse> seats) {
}
