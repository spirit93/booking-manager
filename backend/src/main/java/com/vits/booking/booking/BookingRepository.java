package com.vits.booking.booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, String> {
    boolean existsBySeatIdAndBookingDayAndStatus(String seatId, LocalDate bookingDay, Booking.Status status);

    Optional<Booking> findBySeatIdAndBookingDayAndStatus(String seatId, LocalDate bookingDay, Booking.Status status);

    List<Booking> findAllByBookingDayAndStatus(LocalDate bookingDay, Booking.Status status);
}
