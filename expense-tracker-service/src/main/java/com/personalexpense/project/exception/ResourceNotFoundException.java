package com.personalexpense.project.exception;

public class ResourceNotFoundException extends Exception {

    // Constructor to accept a message for the exception
    public ResourceNotFoundException(String message) {
        super(message); // Pass the message to the superclass constructor
    }
}
