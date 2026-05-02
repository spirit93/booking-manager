package com.vits.booking.seat;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, String> {
    List<Seat> findAllByEnabledTrueOrderByPositionAscLabelAsc();
}
