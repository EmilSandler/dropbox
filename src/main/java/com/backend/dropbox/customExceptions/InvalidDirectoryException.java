package com.backend.dropbox.customExceptions;

public class InvalidDirectoryException extends Exception {
    public InvalidDirectoryException(String message) {
        super(message);
    }
}
