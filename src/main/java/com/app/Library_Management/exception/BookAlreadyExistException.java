package com.app.Library_Management.exception;

public class BookAlreadyExistException extends Exception {
    public BookAlreadyExistException(String message) {
        super(message);
    }
}
