package com.pial.onlinebooklibraryapplication.service;

import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingInfoDto;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;

import java.util.List;

public interface BookBorrowingService {
    public BookBorrowingDto bookBorrowing(Long bookId) throws Exception;
    public BookBorrowingDto bookReturning(Long bookId) throws Exception;

    public List<BookEntity> getAllBookByUser(Long userId) throws Exception;

    public List<BookBorrowingInfoDto> getUserAllHistory(Long userId) throws Exception;

}
