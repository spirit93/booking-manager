package com.vits.booking.booking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record CreateBookingRequest(
        @NotNull(message = "Seat identifier is required.") UUID seatId,
        @NotBlank(message = "Customer email is required.")
        @Email(message = "Customer email must be valid.") String customerEmail,
        @NotNull(message = "Booked day is required.") LocalDate bookedDay
) {
}
