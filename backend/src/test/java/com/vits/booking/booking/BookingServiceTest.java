package com.vits.booking.booking;

import com.vits.booking.common.domain.BookingConflictException;
import com.vits.booking.seat.SeatEntity;
import com.vits.booking.seat.SeatRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final SeatRepository seatRepository = mock(SeatRepository.class);
    private final BookingDayValidator bookingDayValidator = new BookingDayValidator(new BookingDayProperties(LocalDate.parse("2026-01-01"), LocalDate.parse("2026-12-31")));
    private final Clock clock = Clock.fixed(Instant.parse("2026-05-02T00:00:00Z"), ZoneOffset.UTC);
    private final BookingService bookingService = new BookingService(bookingRepository, seatRepository, bookingDayValidator, clock);

    @Test
    void createsActiveBookingForAvailableSeat() {
        UUID seatId = UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9901");
        UUID customerId = UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9910");
        SeatEntity seat = new SeatEntity(seatId, "A1", Instant.now(clock), Instant.now(clock));

        when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));
        when(bookingRepository.existsBySeatIdAndBookedDayAndStatus(seatId, LocalDate.parse("2026-05-02"), BookingStatus.ACTIVE)).thenReturn(false);
        when(bookingRepository.saveAndFlush(any(BookingEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingResponse response = bookingService.createBooking(new CreateBookingRequest(seatId, customerId, LocalDate.parse("2026-05-02")));

        assertThat(response.seatId()).isEqualTo(seatId);
        assertThat(response.customerId()).isEqualTo(customerId);
        assertThat(response.bookedDay()).isEqualTo(LocalDate.parse("2026-05-02"));
        assertThat(response.status()).isEqualTo(BookingStatus.ACTIVE);
        assertThat(response.createdAt()).isEqualTo(Instant.now(clock));
    }

    @Test
    void rejectsAlreadyOccupiedSeatOnSameDay() {
        UUID seatId = UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9901");
        SeatEntity seat = new SeatEntity(seatId, "A1", Instant.now(clock), Instant.now(clock));

        when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));
        when(bookingRepository.existsBySeatIdAndBookedDayAndStatus(seatId, LocalDate.parse("2026-05-02"), BookingStatus.ACTIVE)).thenReturn(true);

        assertThatThrownBy(() -> bookingService.createBooking(new CreateBookingRequest(
                seatId,
                UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9910"),
                LocalDate.parse("2026-05-02")
        ))).isInstanceOf(BookingConflictException.class);
    }
}
