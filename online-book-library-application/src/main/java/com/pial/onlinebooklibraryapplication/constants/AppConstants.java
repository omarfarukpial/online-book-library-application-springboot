package com.pial.onlinebooklibraryapplication.constants;

public class AppConstants {
    public static final String TOKEN_SECRET = "MySecretMySecretMySecretMySecretMySecretMySecretMySecretMySecret";
    public static final long EXPIRATION_TIME = 864000000; //10 days

    public static final String SIGN_IN = "/user/login";
    public static final String SIGN_UP = "/user/register";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String STATUS_BORROWED = "BORROWED";
    public static final String STATUS_AVAILABLE = "AVAILABLE";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_CANCEL = "CANCEL";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String BOOK_NOTFOUND = "Book does not exits!";
    public static final String BOOK_NOTBORROWED = "You did not currently borrowing this book!";
    public static final String BOOK_UNAVAILABLE = "Currently unavailable, but you can reserve this book!";
    public static final String BOOK_AVAILABLE = "This book is already available, you can borrow this!";
    public static final String BOOK_RESERVEDBYYOU = "You already reserved the book before!";
    public static final String BOOK_RESERVATION_NOTFOUND = "No reservation found for this book and user!";
    public static final String BOOK_REVIEWED_BEFORE = "You gave review already, you can update your review!";
    public static final String BOOK_REVIEWED_NOTFOUND = "This review does not exists!";
    public static final String BOOK_BORROWEDBYYOU = "You already reserved the book before!";
    public static final String USER_UNAUTHORIZED = "You are not authorized to access this!";
    public static final String USER_NOTFOUND = "User does not exists!";
    public static final String TOKEN_INVALID = "Token validation problem!";
    public static final String EMAIL_DUPLICATE = "Email already exists!";
    public static final String BOOK_NOTDELETE = "This book is in borrowed state, you should not delete it!";


}
