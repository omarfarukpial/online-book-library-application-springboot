package com.pial.onlinebooklibraryapplication.service.implementation;

import com.pial.onlinebooklibraryapplication.constants.AppConstants;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookDto;
import com.pial.onlinebooklibraryapplication.dto.UserDto;
import com.pial.onlinebooklibraryapplication.entity.BookBorrowingEntity;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import com.pial.onlinebooklibraryapplication.repository.BookRepository;
import com.pial.onlinebooklibraryapplication.service.BookService;
import com.pial.onlinebooklibraryapplication.utils.JWTUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class BookServiceImplementation implements BookService {

    @Autowired
    private BookRepository bookRepository;



    public BookDto createBook(BookDto book) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        BookEntity bookEntity = new BookEntity();

        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setStatus("AVAILABLE");

        BookEntity storedBookDetails = bookRepository.save(bookEntity);
        return modelMapper.map(storedBookDetails,BookDto.class);
    }

    public List<BookEntity> getAllBook() throws Exception {
        return bookRepository.findAll();
    }

    public void deleteBook(BookDto bookDto) throws Exception {
        if (!bookRepository.existsByBookId(bookDto.getBookId())) throw new Exception("Book does not exists!");
        bookRepository.deleteByBookId(bookDto.getBookId());
    }

    public BookDto updateBook(BookDto book) throws Exception {
        if (!bookRepository.existsByBookId(book.getBookId())) throw new Exception("Book does not exists!");

        ModelMapper modelMapper = new ModelMapper();
        BookEntity bookEntity = bookRepository.findByBookId(book.getBookId());
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        BookEntity storedBookDetails = bookRepository.save(bookEntity);
        return modelMapper.map(storedBookDetails,BookDto.class);
    }

    public BookDto getBookByBookId(Long bookId) throws Exception {
        if (!bookRepository.existsByBookId(bookId)) throw new Exception("Book does not exists!");
        BookDto returnValue = new BookDto();
        BookEntity bookEntity = bookRepository.findByBookId(bookId);
        BeanUtils.copyProperties(bookEntity, returnValue);
        return returnValue;
    }


}
