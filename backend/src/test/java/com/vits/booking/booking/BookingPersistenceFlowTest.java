package com.vits.booking.booking;

import static org.assertj.core.api.Assertions.assertThat;

import com.vits.booking.seat.SeatService;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@ActiveProfiles("test")
class BookingPersistenceFlowTest {
    @Autowired
    BookingService bookingService;

    @Autowired
    SeatService seatService;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    @AfterEach
    void cleanBookings() {
        bookingRepository.deleteAll();
    }

    @Test
    void createdBookingIsReturnedByLaterAvailabilityReadForSameDayOnly() {
        var day = LocalDate.of(2026, 5, 2);
        transactionTemplate.executeWithoutResult(status -> bookingService.create(new BookingRequest("A2", day)));

        var sameDay = seatService.availabilityFor(day);
        var nextDay = seatService.availabilityFor(day.plusDays(1));

        assertThat(sameDay.seats())
                .filteredOn(item -> item.seatId().equals("A2"))
                .singleElement()
                .satisfies(item -> {
                    assertThat(item.status()).isEqualTo("OCCUPIED");
                    assertThat(item.bookingId()).isNotBlank();
                });
        assertThat(nextDay.seats())
                .filteredOn(item -> item.seatId().equals("A2"))
                .singleElement()
                .extracting(item -> item.status())
                .isEqualTo("AVAILABLE");
    }
}
