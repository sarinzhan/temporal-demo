package com.example.temporaldemo.ownworkflowengine;

public class ExternalClientUnavailableException extends RuntimeException {
    public ExternalClientUnavailableException(String message) {
        super(message);
    }
}
