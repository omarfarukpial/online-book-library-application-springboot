package com.pial.onlinebooklibraryapplication.service.implementation;

import com.pial.onlinebooklibraryapplication.constants.AppConstants;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingInfoDto;
import com.pial.onlinebooklibraryapplication.dto.BookDto;
import com.pial.onlinebooklibraryapplication.dto.UserDto;
import com.pial.onlinebooklibraryapplication.entity.BookBorrowingEntity;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import com.pial.onlinebooklibraryapplication.exception.BookIdNotFoundException;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImplementation implements BookService {

    @Autowired
    private BookRepository bookRepository;



    public BookDto createBook(BookDto book) {
        ModelMapper modelMapper = new ModelMapper();
        BookEntity bookEntity = new BookEntity();

        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setStatus("AVAILABLE");
        bookEntity.setDeleted(false);

        BookEntity storedBookDetails = bookRepository.save(bookEntity);
        return modelMapper.map(storedBookDetails,BookDto.class);
    }

    public List<BookDto> getAllBook(){

        List<BookEntity> allBooks = bookRepository.findAllByDeletedFalse();

        List<BookDto> bookDtoList = allBooks.stream()
                .map(bookEntity -> BookDto.builder()
                        .bookId(bookEntity.getBookId())
                        .title(bookEntity.getTitle())
                        .author(bookEntity.getAuthor())
                        .status(bookEntity.getStatus())
                        .build()
                )
                .collect(Collectors.toList());


        return bookDtoList;
    }

    public void deleteBook(BookDto bookDto) throws Exception {
        Optional<BookEntity> optionalBook = bookRepository.findByBookIdAndDeletedFalse(bookDto.getBookId());

        if (optionalBook.isPresent()) {
            BookEntity bookEntity = optionalBook.get();
            if(bookEntity.getStatus().equals("BORROWED")) throw new Exception("This book is in borrowed state, you should not delete it!");
            bookEntity.setDeleted(true);
            bookRepository.save(bookEntity);
        } else {
            throw new Exception("Book does not exists!");
        }
    }

    public BookDto updateBook(BookDto book) throws BookIdNotFoundException {

        ModelMapper modelMapper = new ModelMapper();
        BookEntity bookEntity = bookRepository.findByBookId(book.getBookId());

        if (bookEntity == null || bookEntity.isDeleted())  throw new BookIdNotFoundException("Book does not exists!");;

        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        BookEntity storedBookDetails = bookRepository.save(bookEntity);
        return modelMapper.map(storedBookDetails,BookDto.class);
    }



}
