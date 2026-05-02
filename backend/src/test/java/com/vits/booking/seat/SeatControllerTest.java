package com.vits.booking.seat;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vits.booking.booking.BookingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SeatControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BookingRepository bookingRepository;

    @AfterEach
    void cleanBookings() {
        bookingRepository.deleteAll();
    }

    @Test
    void returnsAvailabilityForValidDay() throws Exception {
        mockMvc.perform(get("/api/seats/availability").param("day", "2026-05-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.day").value("2026-05-02"))
                .andExpect(jsonPath("$.seats", hasSize(6)));
    }

    @Test
    void rejectsInvalidDay() throws Exception {
        mockMvc.perform(get("/api/seats/availability").param("day", "not-a-day"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
