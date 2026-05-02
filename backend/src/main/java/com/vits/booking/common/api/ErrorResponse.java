package com.vits.booking.common.api;

import java.util.Map;

public record ErrorResponse(String code, String message, Map<String, Object> details) {

    public ErrorResponse(String code, String message) {
        this(code, message, Map.of());
    }
}
