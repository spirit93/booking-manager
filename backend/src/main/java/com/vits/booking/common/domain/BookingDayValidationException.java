package com.vits.booking.common.domain;

public class BookingDayValidationException extends RuntimeException {

    private final String field;

    public BookingDayValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String field() {
        return field;
    }
}
