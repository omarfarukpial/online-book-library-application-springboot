package com.pial.onlinebooklibraryapplication.service;

import com.pial.onlinebooklibraryapplication.dto.BookDto;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;

import java.util.List;

public interface BookService {

    BookDto createBook(BookDto bookDto) throws Exception;
//    BookDto updateBook(BookDto bookDto) throws Exception;
//    BookDto deleteBook(BookDto bookDto) throws Exception;
    List<BookEntity> getAllBook() throws Exception;

}
