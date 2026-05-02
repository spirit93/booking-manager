package com.vits.booking.seat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/seats")
public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/availability")
    SeatAvailabilityResponse availability(@RequestParam @NotNull LocalDate day) {
        return seatService.availabilityFor(day);
    }
}
