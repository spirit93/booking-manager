package com.vits.booking.common.domain;

import java.util.Map;

public class BookingConflictException extends RuntimeException {

    private final Map<String, Object> details;

    public BookingConflictException(String message, Map<String, Object> details) {
        super(message);
        this.details = details;
    }

    public Map<String, Object> details() {
        return details;
    }
}
