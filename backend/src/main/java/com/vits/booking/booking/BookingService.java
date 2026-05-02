package com.vits.booking.booking;

import com.vits.booking.common.domain.BookingConflictException;
import com.vits.booking.common.domain.BookingServiceUnavailableException;
import com.vits.booking.common.domain.ResourceNotFoundException;
import com.vits.booking.seat.SeatEntity;
import com.vits.booking.seat.SeatRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final BookingDayValidator bookingDayValidator;
    private final Clock clock;

    @Autowired
    public BookingService(BookingRepository bookingRepository, SeatRepository seatRepository, BookingDayValidator bookingDayValidator) {
        this(bookingRepository, seatRepository, bookingDayValidator, Clock.systemUTC());
    }

    BookingService(BookingRepository bookingRepository, SeatRepository seatRepository, BookingDayValidator bookingDayValidator, Clock clock) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.bookingDayValidator = bookingDayValidator;
        this.clock = clock;
    }

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        LocalDate bookedDay = bookingDayValidator.validate(request.bookedDay(), "bookedDay");
        try {
            SeatEntity seat = seatRepository.findById(request.seatId())
                    .orElseThrow(() -> new ResourceNotFoundException("SEAT_NOT_FOUND", "Selected seat was not found."));

            if (bookingRepository.existsBySeatIdAndBookedDayAndStatus(request.seatId(), bookedDay, BookingStatus.ACTIVE)) {
                log.info("booking_conflict seatId={} bookedDay={} reason=already_active", request.seatId(), bookedDay);
                throw seatUnavailable(request.seatId(), bookedDay);
            }

            Instant now = Instant.now(clock);
            BookingEntity booking = new BookingEntity(
                    UUID.randomUUID(),
                    seat,
                    request.customerId(),
                    bookedDay,
                    BookingStatus.ACTIVE,
                    now,
                    now
            );
            return BookingResponse.from(bookingRepository.saveAndFlush(booking));
        } catch (BookingConflictException | ResourceNotFoundException exception) {
            throw exception;
        } catch (DataIntegrityViolationException exception) {
            log.info("booking_conflict seatId={} bookedDay={} reason=database_unique_constraint", request.seatId(), bookedDay);
            throw seatUnavailable(request.seatId(), bookedDay);
        } catch (DataAccessException exception) {
            log.warn("booking_temporary_failure seatId={} bookedDay={}", request.seatId(), bookedDay, exception);
            throw new BookingServiceUnavailableException("Booking could not be completed.", exception);
        }
    }

    private BookingConflictException seatUnavailable(UUID seatId, LocalDate bookedDay) {
        return new BookingConflictException(
                "Selected seat is no longer available for " + bookedDay + ".",
                Map.of("seatId", seatId, "bookedDay", bookedDay)
        );
    }
}
