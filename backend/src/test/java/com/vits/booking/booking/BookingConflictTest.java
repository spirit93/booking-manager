package com.vits.booking.booking;

import com.vits.booking.common.domain.BookingConflictException;
import com.vits.booking.seat.SeatEntity;
import com.vits.booking.seat.SeatRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingConflictTest {

    @Test
    void duplicateActiveBookingIsRejectedBeforePersisting() {
        BookingRepository bookingRepository = mock(BookingRepository.class);
        SeatRepository seatRepository = mock(SeatRepository.class);
        BookingDayValidator bookingDayValidator = new BookingDayValidator(new BookingDayProperties(LocalDate.parse("2026-01-01"), LocalDate.parse("2026-12-31")));
        BookingService bookingService = new BookingService(bookingRepository, seatRepository, bookingDayValidator);
        UUID seatId = UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9901");

        when(seatRepository.findById(seatId)).thenReturn(java.util.Optional.of(new SeatEntity(seatId, "A1", Instant.now(), Instant.now())));
        when(bookingRepository.existsBySeatIdAndBookedDayAndStatus(seatId, LocalDate.parse("2026-05-02"), BookingStatus.ACTIVE)).thenReturn(true);

        assertThatThrownBy(() -> bookingService.createBooking(new CreateBookingRequest(
                seatId,
                "customer@example.com",
                LocalDate.parse("2026-05-02")
        ))).isInstanceOf(BookingConflictException.class);
    }
}
