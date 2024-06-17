package com.booking_api.core.exception;

public class DateConflictException extends RuntimeException {
    public DateConflictException(String message) {
        super(message);
    }
}
