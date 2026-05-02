package com.vits.booking.seat;

import com.vits.booking.common.api.GlobalExceptionHandler;
import com.vits.booking.common.domain.BookingServiceUnavailableException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeatController.class)
@Import(GlobalExceptionHandler.class)
class SeatControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private SeatService seatService;

    @Test
    void listsSeats() throws Exception {
        when(seatService.listSeats()).thenReturn(List.of(
                new SeatResponse(UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9901"), "A1", SeatStatus.AVAILABLE),
                new SeatResponse(UUID.fromString("018f6ff5-9055-7c82-b0de-83cfd0bd9902"), "A2", SeatStatus.OCCUPIED)
        ));

        mvc.perform(get("/api/seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$[1].status").value("OCCUPIED"));
    }

    @Test
    void mapsServiceFailureTo503() throws Exception {
        when(seatService.listSeats()).thenThrow(new BookingServiceUnavailableException("down"));

        mvc.perform(get("/api/seats"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("SERVICE_UNAVAILABLE"));
    }
}
