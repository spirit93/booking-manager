package com.vits.booking.common;

import java.util.Map;

public record ApiError(String code, String message, Map<String, String> details) {
    public ApiError(String code, String message) {
        this(code, message, Map.of());
    }
}
