package com.vits.booking.seat;

import com.vits.booking.booking.Booking;
import com.vits.booking.booking.BookingRepository;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    public SeatService(SeatRepository seatRepository, BookingRepository bookingRepository) {
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public SeatAvailabilityResponse availabilityFor(LocalDate day) {
        Map<String, Booking> bookingsBySeat = bookingRepository.findAllByBookingDayAndStatus(day, Booking.Status.ACTIVE)
                .stream()
                .collect(Collectors.toMap(booking -> booking.getSeat().getId(), Function.identity()));

        var seats = seatRepository.findAllByEnabledTrueOrderByPositionAscLabelAsc().stream()
                .map(seat -> {
                    Booking booking = bookingsBySeat.get(seat.getId());
                    if (booking == null) {
                        return new SeatAvailabilityResponse.SeatAvailabilityItem(
                                seat.getId(), seat.getLabel(), "AVAILABLE", null);
                    }
                    return new SeatAvailabilityResponse.SeatAvailabilityItem(
                            seat.getId(), seat.getLabel(), "OCCUPIED", booking.getId());
                })
                .toList();

        return new SeatAvailabilityResponse(day, seats);
    }
}
