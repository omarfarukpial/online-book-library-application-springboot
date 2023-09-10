package com.pial.onlinebooklibraryapplication.controller;


import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingInfoDto;
import com.pial.onlinebooklibraryapplication.dto.BookDto;
import com.pial.onlinebooklibraryapplication.dto.BookReviewDto;
import com.pial.onlinebooklibraryapplication.entity.BookBorrowingEntity;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.service.implementation.BookBorrowingServiceImplementation;
import com.pial.onlinebooklibraryapplication.service.implementation.BookReviewServiceImplementation;
import com.pial.onlinebooklibraryapplication.service.implementation.BookServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookServiceImplementation bookServiceImplementation;

    @Autowired
    private BookBorrowingServiceImplementation bookBorrowingServiceImplementation;

    @Autowired
    private BookReviewServiceImplementation bookReviewServiceImplementation;


    @PostMapping ("/books/create")
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto) {
        try {
            BookDto newBook = bookServiceImplementation.createBook(bookDto);
            return new  ResponseEntity<>(newBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/books/update")
    public ResponseEntity<?> updateBook(@RequestBody BookDto bookDto) {
        try {
            BookDto updatedBook = bookServiceImplementation.updateBook(bookDto);
            return new  ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/books/delete")
    public ResponseEntity<?> deleteBook(@RequestBody BookDto bookDto) {
        try {
            bookServiceImplementation.deleteBook(bookDto);
            return new  ResponseEntity<>("Book Deleted!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/books/all")
    public ResponseEntity<?> allBooks() {
        try {
            List <BookEntity> allBook = bookServiceImplementation.getAllBook();
            return new  ResponseEntity<>(allBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/books/{bookId}/borrow")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId) {
        try {
            BookBorrowingDto book = bookBorrowingServiceImplementation.bookBorrowing(bookId);
            return new  ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/books/{bookId}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId) {
        try {
            BookBorrowingDto book = bookBorrowingServiceImplementation.bookReturning(bookId);
            return new  ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/users/{userId}/books")
    public ResponseEntity<?> retriveBooks(@PathVariable Long userId) {
        try {
            List<BookEntity> allBookByUser = bookBorrowingServiceImplementation.getAllBookByUser(userId);
            return new ResponseEntity<>(allBookByUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{userId}/borrowed-books")
    public ResponseEntity<?> retriveBorrowedBooks(@PathVariable Long userId) {
        try {
            List<BookEntity> allBookByUser = bookBorrowingServiceImplementation.getAllBorrowedBookByUser(userId);
            return new ResponseEntity<>(allBookByUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/users/{userId}/history")
    public ResponseEntity<?> userHistory(@PathVariable Long userId) {
        try {
            List<BookBorrowingInfoDto> userAllHistory = bookBorrowingServiceImplementation.getUserAllHistory(userId);
            return new ResponseEntity<>(userAllHistory, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/books/{bookId}/reviews/create")
    public ResponseEntity<?> createReview(@PathVariable Long bookId, @RequestBody BookReviewDto bookReviewDto) {
        try {
            BookReviewDto newReview =  bookReviewServiceImplementation.createBookReview(bookId, bookReviewDto);
            return new ResponseEntity<>(newReview, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<?> allReview(@PathVariable Long bookId) {
        try {
            List <BookReviewDto> newReview =  bookReviewServiceImplementation.allBookReview(bookId);
            return new ResponseEntity<>(newReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/books/{bookId}/reviews/{reviewId}/delete")
    public ResponseEntity<?> deleteReview (@PathVariable Long bookId, @PathVariable Long reviewId) {
        try {
            bookReviewServiceImplementation.deleteReview(bookId, reviewId);
            return new  ResponseEntity<>("Review Deleted!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/books/{bookId}/reviews/{reviewId}/update")
    public ResponseEntity<?> updateReview (@PathVariable Long bookId, @PathVariable Long reviewId, @RequestBody BookReviewDto bookReviewDto) {
        try {
            BookReviewDto updatedReview =  bookReviewServiceImplementation.updateReview(bookId, reviewId, bookReviewDto);
            return new  ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



}
