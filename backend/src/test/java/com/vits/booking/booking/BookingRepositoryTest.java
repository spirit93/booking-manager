package com.vits.booking.booking;

import static org.assertj.core.api.Assertions.assertThat;

import com.vits.booking.seat.SeatRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BookingRepositoryTest {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    SeatRepository seatRepository;

    @AfterEach
    void cleanBookings() {
        bookingRepository.deleteAll();
    }

    @Test
    void filtersActiveBookingsByDay() {
        var seat = seatRepository.findById("A2").orElseThrow();
        var firstDay = LocalDate.of(2026, 5, 2);
        var secondDay = LocalDate.of(2026, 5, 3);
        bookingRepository.save(new Booking(UUID.randomUUID().toString(), seat, firstDay, Booking.Status.ACTIVE, Instant.now()));
        bookingRepository.save(new Booking(UUID.randomUUID().toString(), seat, secondDay, Booking.Status.ACTIVE, Instant.now()));

        assertThat(bookingRepository.findAllByBookingDayAndStatus(firstDay, Booking.Status.ACTIVE))
                .singleElement()
                .extracting(Booking::getBookingDay)
                .isEqualTo(firstDay);
    }
}
