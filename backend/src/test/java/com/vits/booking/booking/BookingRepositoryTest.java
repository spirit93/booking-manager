package com.vits.booking.booking;

import com.vits.booking.seat.SeatEntity;
import com.vits.booking.seat.SeatRepository;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Test
    void persistsActiveBookings() {
        Instant now = Instant.now();
        SeatEntity seat = seatRepository.save(new SeatEntity(
                UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9901"),
                "A1",
                now,
                now
        ));
        BookingEntity saved = bookingRepository.save(new BookingEntity(
                UUID.randomUUID(),
                seat,
                UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9910"),
                BookingStatus.ACTIVE,
                now,
                now
        ));

        assertThat(bookingRepository.existsById(saved.getId())).isTrue();
        assertThat(bookingRepository.existsBySeatIdAndStatus(seat.getId(), BookingStatus.ACTIVE)).isTrue();
    }
}
