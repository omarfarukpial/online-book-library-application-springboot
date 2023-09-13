package com.pial.onlinebooklibraryapplication.service;

import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingInfoDto;
import com.pial.onlinebooklibraryapplication.dto.BookDto;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;

import java.util.List;

public interface BookBorrowingService {
    BookBorrowingDto bookBorrowing(Long bookId) throws Exception;
    BookBorrowingDto bookReturning(Long bookId) throws Exception;
    List<BookDto> getAllBookByUser(Long userId) throws Exception;
    List<BookBorrowingInfoDto> getUserAllHistory(Long userId) throws Exception;
    List<BookDto> getAllBorrowedBookByUser(Long userId) throws Exception;
}
