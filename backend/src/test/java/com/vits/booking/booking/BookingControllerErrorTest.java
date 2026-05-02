package com.vits.booking.booking;

import com.vits.booking.common.api.GlobalExceptionHandler;
import com.vits.booking.common.domain.BookingConflictException;
import com.vits.booking.common.domain.BookingServiceUnavailableException;
import com.vits.booking.common.domain.ResourceNotFoundException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.vits.booking.support.MvcTestSupport.BOOKED_DAY;
import static com.vits.booking.support.MvcTestSupport.CUSTOMER_EMAIL;
import static com.vits.booking.support.MvcTestSupport.SEAT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@Import(GlobalExceptionHandler.class)
class BookingControllerErrorTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookingService bookingService;

    @Test
    void mapsValidationErrors() throws Exception {
        mvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void rejectsInvalidCustomerEmail() throws Exception {
        mvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"seatId":"%s","customerEmail":"not-an-email","bookedDay":"%s"}
                                """.formatted(SEAT_ID, BOOKED_DAY)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("customerEmail"));
    }

    @Test
    void mapsMissingSeat() throws Exception {
        when(bookingService.createBooking(any())).thenThrow(new ResourceNotFoundException("SEAT_NOT_FOUND", "Selected seat was not found."));

        performValidPost().andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("SEAT_NOT_FOUND"));
    }

    @Test
    void mapsConflict() throws Exception {
        when(bookingService.createBooking(any())).thenThrow(new BookingConflictException("Selected seat is no longer available for " + BOOKED_DAY + ".", Map.of("seatId", SEAT_ID, "bookedDay", BOOKED_DAY)));

        performValidPost().andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("BOOKING_CONFLICT"))
                .andExpect(jsonPath("$.details.bookedDay").value(BOOKED_DAY));
    }

    @Test
    void mapsTemporaryFailure() throws Exception {
        when(bookingService.createBooking(any())).thenThrow(new BookingServiceUnavailableException("down"));

        performValidPost().andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("SERVICE_UNAVAILABLE"));
    }

    private org.springframework.test.web.servlet.ResultActions performValidPost() throws Exception {
        return mvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"seatId":"%s","customerEmail":"%s","bookedDay":"%s"}
                        """.formatted(SEAT_ID, CUSTOMER_EMAIL, BOOKED_DAY)));
    }
}
