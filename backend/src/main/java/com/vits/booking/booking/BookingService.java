package com.vits.booking.booking;

import com.vits.booking.common.domain.BookingConflictException;
import com.vits.booking.common.domain.BookingServiceUnavailableException;
import com.vits.booking.common.domain.ResourceNotFoundException;
import com.vits.booking.seat.SeatEntity;
import com.vits.booking.seat.SeatRepository;
import java.time.Clock;
import java.time.Instant;
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
    private final Clock clock;

    @Autowired
    public BookingService(BookingRepository bookingRepository, SeatRepository seatRepository) {
        this(bookingRepository, seatRepository, Clock.systemUTC());
    }

    BookingService(BookingRepository bookingRepository, SeatRepository seatRepository, Clock clock) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.clock = clock;
    }

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        try {
            SeatEntity seat = seatRepository.findById(request.seatId())
                    .orElseThrow(() -> new ResourceNotFoundException("SEAT_NOT_FOUND", "Selected seat was not found."));

            if (bookingRepository.existsBySeatIdAndStatus(request.seatId(), BookingStatus.ACTIVE)) {
                log.info("booking_conflict seatId={} reason=already_active", request.seatId());
                throw seatUnavailable(request.seatId());
            }

            Instant now = Instant.now(clock);
            BookingEntity booking = new BookingEntity(
                    UUID.randomUUID(),
                    seat,
                    request.customerId(),
                    BookingStatus.ACTIVE,
                    now,
                    now
            );
            return BookingResponse.from(bookingRepository.saveAndFlush(booking));
        } catch (BookingConflictException | ResourceNotFoundException exception) {
            throw exception;
        } catch (DataIntegrityViolationException exception) {
            log.info("booking_conflict seatId={} reason=database_unique_constraint", request.seatId());
            throw seatUnavailable(request.seatId());
        } catch (DataAccessException exception) {
            log.warn("booking_temporary_failure seatId={}", request.seatId(), exception);
            throw new BookingServiceUnavailableException("Booking could not be completed.", exception);
        }
    }

    private BookingConflictException seatUnavailable(UUID seatId) {
        return new BookingConflictException(
                "Selected seat is no longer available.",
                Map.of("seatId", seatId)
        );
    }
}
