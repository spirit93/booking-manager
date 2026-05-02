package com.vits.booking.seat;

import java.util.UUID;

public interface SeatAvailabilityProjection {
    UUID getId();

    String getLabel();

    boolean isOccupied();
}
