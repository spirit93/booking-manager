package com.vits.booking.seat;

import java.util.UUID;

public record SeatResponse(UUID id, String label, SeatStatus status) {
}
