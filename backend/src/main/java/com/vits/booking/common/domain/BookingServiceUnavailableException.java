package com.vits.booking.common.domain;

public class BookingServiceUnavailableException extends RuntimeException {

    public BookingServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingServiceUnavailableException(String message) {
        super(message);
    }
}
