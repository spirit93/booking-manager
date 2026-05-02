package com.vits.booking.seat;

import static org.assertj.core.api.Assertions.assertThat;

import com.vits.booking.booking.Booking;
import com.vits.booking.booking.BookingRepository;
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
class SeatServiceTest {
    @Autowired
    SeatService seatService;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    BookingRepository bookingRepository;

    @AfterEach
    void cleanBookings() {
        bookingRepository.deleteAll();
    }

    @Test
    void mapsAvailabilityOnlyForRequestedDay() {
        var seat = seatRepository.findById("A3").orElseThrow();
        bookingRepository.save(new Booking(UUID.randomUUID().toString(), seat, LocalDate.of(2026, 5, 2), Booking.Status.ACTIVE, Instant.now()));

        var occupiedDay = seatService.availabilityFor(LocalDate.of(2026, 5, 2));
        var freeDay = seatService.availabilityFor(LocalDate.of(2026, 5, 3));

        assertThat(occupiedDay.seats())
                .filteredOn(item -> item.seatId().equals("A3"))
                .singleElement()
                .extracting(SeatAvailabilityResponse.SeatAvailabilityItem::status)
                .isEqualTo("OCCUPIED");
        assertThat(freeDay.seats())
                .filteredOn(item -> item.seatId().equals("A3"))
                .singleElement()
                .extracting(SeatAvailabilityResponse.SeatAvailabilityItem::status)
                .isEqualTo("AVAILABLE");
    }
}
