package com.pial.onlinebooklibraryapplication.service;

import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookDto;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto bookDto) throws Exception;
    List <BookDto> getAllBook() throws Exception;
    BookDto updateBook(BookDto bookDto) throws Exception;
    void deleteBook(BookDto bookDto) throws Exception;
}
