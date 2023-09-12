package com.pial.onlinebooklibraryapplication.controller;

import com.pial.onlinebooklibraryapplication.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({BookAlreadyExistsException.class})
    public String handleBookAlreadyExistsException(String MESSAGE) {
        return MESSAGE;
    }

    @ExceptionHandler({BookIdNotFoundException.class})
    public String handleBookIdNotFoundException(String MESSAGE) {
        return MESSAGE;
    }

    @ExceptionHandler({BookIdOrReviewIdMismatchException.class})
    public String handleBookIdOrReviewIdMismatchException(String MESSAGE) {
        return MESSAGE;
    }

    @ExceptionHandler({BookNotBorrowedException.class})
    public String handleBookNotBorrowedException(String MESSAGE) {
        return MESSAGE;
    }

    @ExceptionHandler({BookNotReservedException.class})
    public String handleBookNotReservedException(String MESSAGE) {
        return MESSAGE;
    }

    @ExceptionHandler({BookReservedBeforeException.class})
    public String handleBookReservedBeforeException(String MESSAGE) {
        return MESSAGE;
    }

    @ExceptionHandler({BookUnavailableException.class})
    public String handleBookUnavailableException(String MESSAGE) {
        return MESSAGE;
    }

    @ExceptionHandler({EmailAlreadyExistsException.class})
    public String handleEmailAlreadyExistsException(String MESSAGE) {
        return MESSAGE;
    }
    @ExceptionHandler({FormException.class})
    public String handleFormException(String MESSAGE) {
        return MESSAGE;
    }

    @ExceptionHandler({NotAuthorizedException.class})
    public String handleNotAuthorizedException(String MESSAGE) {
        return MESSAGE;
    }
    @ExceptionHandler({ReviewAlreadyExists.class})
    public String handleReviewAlreadyExists(String MESSAGE) {
        return MESSAGE;
    }

    @ExceptionHandler({ReviewIdNotFoundException.class})
    public String handleReviewIdNotFoundException(String MESSAGE) {
        return MESSAGE;
    }

    @ExceptionHandler({UserIdNotFoundException.class})
    public String handleUserIdNotFoundException(String MESSAGE) {
        return MESSAGE;
    }

}
