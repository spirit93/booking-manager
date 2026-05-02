package com.vits.booking.booking;

import com.vits.booking.common.ApiException;
import com.vits.booking.seat.Seat;
import com.vits.booking.seat.SeatRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final Clock clock = Clock.systemUTC();

    public BookingService(SeatRepository seatRepository, BookingRepository bookingRepository) {
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public BookingResponse create(BookingRequest request) {
        Seat seat = seatRepository.findById(request.seatId())
                .filter(Seat::isEnabled)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "SEAT_NOT_FOUND", "Seat is not bookable."));

        if (bookingRepository.existsBySeatIdAndBookingDayAndStatus(seat.getId(), request.day(), Booking.Status.ACTIVE)) {
            log.info("Booking conflict seatId={} day={}", seat.getId(), request.day());
            throw new ApiException(HttpStatus.CONFLICT, "SEAT_ALREADY_BOOKED", "Seat is already occupied for this day.");
        }

        try {
            Booking booking = bookingRepository.save(new Booking(
                    UUID.randomUUID().toString(),
                    seat,
                    request.day(),
                    Booking.Status.ACTIVE,
                    Instant.now(clock)));
            log.info("Booking created bookingId={} seatId={} day={}", booking.getId(), seat.getId(), request.day());
            return BookingResponse.from(booking);
        } catch (DataIntegrityViolationException exception) {
            log.info("Booking conflict after persistence seatId={} day={}", seat.getId(), request.day());
            throw new ApiException(HttpStatus.CONFLICT, "SEAT_ALREADY_BOOKED", "Seat is already occupied for this day.");
        } catch (RuntimeException exception) {
            log.warn("Booking creation failed seatId={} day={}", seat.getId(), request.day());
            throw exception;
        }
    }
}
