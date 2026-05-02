package com.vits.booking.booking;

import com.vits.booking.common.domain.BookingDayValidationException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class BookingDayValidator {

    private final BookingDayProperties properties;

    public BookingDayValidator(BookingDayProperties properties) {
        this.properties = properties;
    }

    public LocalDate validate(LocalDate day) {
        return validate(day, "day");
    }

    public LocalDate validate(LocalDate day, String field) {
        if (day == null) {
            throw new ConstraintViolationException("Booking day is required.", Set.of());
        }
        if (day.isBefore(properties.min()) || day.isAfter(properties.max())) {
            throw new BookingDayValidationException(field, "Booking day must be between " + properties.min() + " and " + properties.max() + ".");
        }
        return day;
    }
}
