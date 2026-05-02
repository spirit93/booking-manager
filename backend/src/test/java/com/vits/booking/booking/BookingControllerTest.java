package com.vits.booking.booking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BookingRepository bookingRepository;

    @AfterEach
    void cleanBookings() {
        bookingRepository.deleteAll();
    }

    @Test
    void createsBooking() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"seatId\":\"B3\",\"day\":\"2026-05-02\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.startsWith("/api/bookings/")))
                .andExpect(jsonPath("$.seatId").value("B3"))
                .andExpect(jsonPath("$.day").value("2026-05-02"));
    }

    @Test
    void rejectsInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"seatId\":\"\",\"day\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void returnsNotFoundForUnknownSeat() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"seatId\":\"Z9\",\"day\":\"2026-05-02\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("SEAT_NOT_FOUND"));
    }

    @Test
    void returnsConflictForDuplicateBooking() throws Exception {
        var body = "{\"seatId\":\"A1\",\"day\":\"2026-05-02\"}";
        mockMvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("SEAT_ALREADY_BOOKED"));
    }
}
