package com.smartcampus.smartcampusapi.exceptions;

public class SensorUnavailableException extends RuntimeException {
    public SensorUnavailableException(String message) {
        super(message);
    }
}