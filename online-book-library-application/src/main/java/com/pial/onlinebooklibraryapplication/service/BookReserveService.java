package com.pial.onlinebooklibraryapplication.service;

import com.pial.onlinebooklibraryapplication.dto.BookReserveDto;
import com.pial.onlinebooklibraryapplication.dto.BookReviewDto;

public interface BookReserveService {
    BookReserveDto reserveBook(Long bookId) throws Exception;
    BookReserveDto cancelReserveBook(Long bookId) throws Exception;
}
