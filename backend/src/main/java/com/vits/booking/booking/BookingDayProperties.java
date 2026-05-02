package com.vits.booking.booking;

import java.time.LocalDate;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "booking.days")
public record BookingDayProperties(LocalDate min, LocalDate max) {
}
