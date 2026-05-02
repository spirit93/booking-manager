package com.vits.booking.seat;

import com.vits.booking.common.domain.BookingServiceUnavailableException;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional(readOnly = true)
    public List<SeatResponse> listSeats() {
        try {
            return seatRepository.findAvailability().stream()
                    .map(SeatMapper::toResponse)
                    .toList();
        } catch (DataAccessException exception) {
            throw new BookingServiceUnavailableException("Seat availability could not be loaded.", exception);
        }
    }
}
