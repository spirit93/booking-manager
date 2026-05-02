package com.vits.booking.booking;

import com.vits.booking.seat.SeatEntity;
import com.vits.booking.seat.SeatRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class ActiveBookingUniquenessTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Test
    void databasePreventsDuplicateActiveBookingsForSameSeat() {
        Instant now = Instant.now();
        SeatEntity seat = seatRepository.save(new SeatEntity(
                UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9901"),
                "A1",
                now,
                now
        ));
        bookingRepository.saveAndFlush(newBooking(seat, LocalDate.parse("2026-05-02")));

        assertThatThrownBy(() -> bookingRepository.saveAndFlush(newBooking(seat, LocalDate.parse("2026-05-02"))))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void databaseAllowsSameSeatOnDifferentDays() {
        Instant now = Instant.now();
        SeatEntity seat = seatRepository.save(new SeatEntity(
                UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9902"),
                "A2",
                now,
                now
        ));
        bookingRepository.saveAndFlush(newBooking(seat, LocalDate.parse("2026-05-02")));

        bookingRepository.saveAndFlush(newBooking(seat, LocalDate.parse("2026-05-03")));
    }

    private BookingEntity newBooking(SeatEntity seat, LocalDate bookedDay) {
        return new BookingEntity(
                UUID.randomUUID(),
                seat,
                "customer@example.com",
                bookedDay,
                BookingStatus.ACTIVE,
                Instant.now(),
                Instant.now()
        );
    }
}
