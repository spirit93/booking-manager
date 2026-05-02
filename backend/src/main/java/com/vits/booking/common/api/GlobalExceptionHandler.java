package com.vits.booking.common.api;

import com.vits.booking.common.domain.BookingDayValidationException;
import com.vits.booking.common.domain.BookingConflictException;
import com.vits.booking.common.domain.BookingServiceUnavailableException;
import com.vits.booking.common.domain.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<ValidationErrorResponse.FieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationErrorResponse.FieldError(error.getField(), error.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(
                "VALIDATION_ERROR",
                "Request contains invalid fields.",
                Map.of(),
                fieldErrors
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ValidationErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        List<ValidationErrorResponse.FieldError> fieldErrors = exception.getConstraintViolations()
                .stream()
                .map(violation -> new ValidationErrorResponse.FieldError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ))
                .toList();
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(
                "VALIDATION_ERROR",
                "Request contains invalid fields.",
                Map.of(),
                fieldErrors
        ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ValidationErrorResponse> handleMissingRequestParameter(MissingServletRequestParameterException exception) {
        return validationError(exception.getParameterName(), "Booking day is required.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ValidationErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        return validationError(exception.getName(), "Booking day must be a valid ISO date.");
    }

    @ExceptionHandler(BookingDayValidationException.class)
    ResponseEntity<ValidationErrorResponse> handleBookingDayValidation(BookingDayValidationException exception) {
        return validationError(exception.field(), exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.code(), exception.getMessage()));
    }

    @ExceptionHandler(BookingConflictException.class)
    ResponseEntity<ErrorResponse> handleConflict(BookingConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("BOOKING_CONFLICT", exception.getMessage(), exception.details()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("BOOKING_CONFLICT", "Selected seat is no longer available."));
    }

    @ExceptionHandler(BookingServiceUnavailableException.class)
    ResponseEntity<ErrorResponse> handleUnavailable(BookingServiceUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse("SERVICE_UNAVAILABLE", "Booking service is temporarily unavailable. Try again later."));
    }

    private ResponseEntity<ValidationErrorResponse> validationError(String field, String message) {
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(
                "VALIDATION_ERROR",
                "Request contains invalid fields.",
                Map.of(),
                List.of(new ValidationErrorResponse.FieldError(field, message))
        ));
    }
}
