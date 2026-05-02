package com.vits.booking.booking;

import com.vits.booking.seat.SeatEntity;
import com.vits.booking.seat.SeatRepository;
import java.time.Instant;
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
        bookingRepository.saveAndFlush(newBooking(seat));

        assertThatThrownBy(() -> bookingRepository.saveAndFlush(newBooking(seat)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private BookingEntity newBooking(SeatEntity seat) {
        return new BookingEntity(
                UUID.randomUUID(),
                seat,
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                Instant.now(),
                Instant.now()
        );
    }
}
