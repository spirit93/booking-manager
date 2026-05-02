package com.vits.booking.seat;

import com.vits.booking.booking.BookingDayValidator;
import com.vits.booking.common.domain.BookingServiceUnavailableException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final BookingDayValidator bookingDayValidator;

    public SeatService(SeatRepository seatRepository, BookingDayValidator bookingDayValidator) {
        this.seatRepository = seatRepository;
        this.bookingDayValidator = bookingDayValidator;
    }

    @Transactional(readOnly = true)
    public SeatAvailabilityResponse listAvailability(LocalDate day) {
        LocalDate validatedDay = bookingDayValidator.validate(day);
        try {
            List<SeatResponse> seats = seatRepository.findAvailabilityByDay(validatedDay).stream()
                    .map(SeatMapper::toResponse)
                    .toList();
            return new SeatAvailabilityResponse(validatedDay, seats);
        } catch (DataAccessException exception) {
            throw new BookingServiceUnavailableException("Seat availability could not be loaded.", exception);
        }
    }
}
