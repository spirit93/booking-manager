package com.vits.booking.booking;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.create(request);
        return ResponseEntity.created(URI.create("/api/bookings/" + response.bookingId())).body(response);
    }
}
