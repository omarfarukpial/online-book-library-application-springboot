package com.pial.onlinebooklibraryapplication.service;

import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookReviewDto;

import java.util.List;

public interface BookReviewService {

    public BookReviewDto createBookReview(Long bookId, BookReviewDto bookReviewDto) throws Exception;
    public List<BookReviewDto> allBookReview(Long bookId) throws Exception;

    public BookReviewDto updateReview(Long bookId, Long reviewId, BookReviewDto bookReviewDto) throws Exception;

    public void deleteReview(Long bookId, Long reviewId) throws Exception;

}
