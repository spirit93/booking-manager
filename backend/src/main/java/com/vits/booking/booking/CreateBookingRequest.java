package com.vits.booking.booking;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateBookingRequest(
        @NotNull(message = "Seat identifier is required.") UUID seatId,
        @NotNull(message = "Customer identifier is required.") UUID customerId
) {
}
