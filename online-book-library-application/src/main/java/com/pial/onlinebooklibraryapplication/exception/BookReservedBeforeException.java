package com.pial.onlinebooklibraryapplication.exception;

public class BookReservedBeforeException extends Exception{
    public BookReservedBeforeException(String MESSAGE) {
        super(MESSAGE);
    }
}
