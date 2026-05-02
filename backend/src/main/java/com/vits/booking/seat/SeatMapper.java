package com.vits.booking.seat;

public final class SeatMapper {

    private SeatMapper() {
    }

    public static SeatResponse toResponse(SeatAvailabilityProjection projection) {
        return new SeatResponse(
                projection.getId(),
                projection.getLabel(),
                projection.isOccupied() ? SeatStatus.OCCUPIED : SeatStatus.AVAILABLE
        );
    }
}
