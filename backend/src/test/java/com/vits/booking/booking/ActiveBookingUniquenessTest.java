package com.vits.booking.booking;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vits.booking.seat.SeatRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ActiveBookingUniquenessTest {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    SeatRepository seatRepository;

    @AfterEach
    void cleanBookings() {
        bookingRepository.deleteAll();
    }

    @Test
    void preventsDuplicateActiveBookingForSameSeatAndDay() {
        var seat = seatRepository.findById("A1").orElseThrow();
        var day = LocalDate.of(2026, 5, 2);
        bookingRepository.saveAndFlush(new Booking(UUID.randomUUID().toString(), seat, day, Booking.Status.ACTIVE, Instant.now()));

        assertThatThrownBy(() -> bookingRepository.saveAndFlush(
                new Booking(UUID.randomUUID().toString(), seat, day, Booking.Status.ACTIVE, Instant.now())))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
