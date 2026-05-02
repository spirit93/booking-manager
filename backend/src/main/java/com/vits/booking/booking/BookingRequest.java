package com.vits.booking.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookingRequest(@NotBlank String seatId, @NotNull LocalDate day) {
}
