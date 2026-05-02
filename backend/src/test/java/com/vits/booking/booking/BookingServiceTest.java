package com.vits.booking.booking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vits.booking.common.ApiException;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BookingServiceTest {
    @Autowired
    BookingService bookingService;

    @Autowired
    BookingRepository bookingRepository;

    @AfterEach
    void cleanBookings() {
        bookingRepository.deleteAll();
    }

    @Test
    void createsBookingForEnabledSeat() {
        var response = bookingService.create(new BookingRequest("B1", LocalDate.of(2026, 5, 2)));

        assertThat(response.bookingId()).isNotBlank();
        assertThat(response.status()).isEqualTo("ACTIVE");
        assertThat(bookingRepository.existsBySeatIdAndBookingDayAndStatus("B1", LocalDate.of(2026, 5, 2), Booking.Status.ACTIVE))
                .isTrue();
    }

    @Test
    void rejectsUnknownSeat() {
        assertThatThrownBy(() -> bookingService.create(new BookingRequest("Z9", LocalDate.of(2026, 5, 2))))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(exception.getCode()).isEqualTo("SEAT_NOT_FOUND");
                });
    }

    @Test
    void rejectsDuplicateBooking() {
        var request = new BookingRequest("B2", LocalDate.of(2026, 5, 2));
        bookingService.create(request);

        assertThatThrownBy(() -> bookingService.create(request))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
                    assertThat(exception.getCode()).isEqualTo("SEAT_ALREADY_BOOKED");
                });
    }
}
