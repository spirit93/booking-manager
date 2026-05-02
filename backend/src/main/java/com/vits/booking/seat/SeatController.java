package com.vits.booking.seat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/seats")
@Validated
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/availability")
    public SeatAvailabilityResponse listAvailability(@RequestParam @NotNull(message = "Booking day is required.") LocalDate day) {
        return seatService.listAvailability(day);
    }
}
