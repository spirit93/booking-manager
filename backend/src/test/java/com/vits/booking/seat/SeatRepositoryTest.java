package com.vits.booking.seat;

import com.vits.booking.booking.BookingEntity;
import com.vits.booking.booking.BookingRepository;
import com.vits.booking.booking.BookingStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SeatRepositoryTest {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void derivesOccupiedSeatsFromActiveBookings() {
        Instant now = Instant.now();
        SeatEntity seat = seatRepository.save(new SeatEntity(
                UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9901"),
                "A1",
                now,
                now
        ));
        bookingRepository.save(new BookingEntity(
                UUID.randomUUID(),
                seat,
                UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9910"),
                LocalDate.parse("2026-05-02"),
                BookingStatus.ACTIVE,
                now,
                now
        ));

        assertThat(seatRepository.findAvailabilityByDay(LocalDate.parse("2026-05-02")))
                .filteredOn(SeatAvailabilityProjection::isOccupied)
                .extracting(SeatAvailabilityProjection::getId)
                .contains(seat.getId());
        assertThat(seatRepository.findAvailabilityByDay(LocalDate.parse("2026-05-03")))
                .filteredOn(SeatAvailabilityProjection::isOccupied)
                .isEmpty();
    }
}
