package com.vits.booking.booking;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {

    boolean existsBySeatIdAndStatus(UUID seatId, BookingStatus status);
}
