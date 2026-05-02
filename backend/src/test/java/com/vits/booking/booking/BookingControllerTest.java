package com.vits.booking.booking;

import com.vits.booking.common.api.GlobalExceptionHandler;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.vits.booking.support.MvcTestSupport.CUSTOMER_ID;
import static com.vits.booking.support.MvcTestSupport.BOOKED_DAY;
import static com.vits.booking.support.MvcTestSupport.SEAT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@Import(GlobalExceptionHandler.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookingService bookingService;

    @Test
    void createsBooking() throws Exception {
        when(bookingService.createBooking(any())).thenReturn(new BookingResponse(
                UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9920"),
                UUID.fromString(SEAT_ID),
                UUID.fromString(CUSTOMER_ID),
                LocalDate.parse(BOOKED_DAY),
                BookingStatus.ACTIVE,
                Instant.parse("2026-05-02T00:00:00Z")
        ));

        mvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"seatId":"%s","customerId":"%s","bookedDay":"%s"}
                                """.formatted(SEAT_ID, CUSTOMER_ID, BOOKED_DAY)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/bookings/018f6ff5-9055-7c82-b0de-83cfd0bd9920"))
                .andExpect(jsonPath("$.bookedDay").value(BOOKED_DAY))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
