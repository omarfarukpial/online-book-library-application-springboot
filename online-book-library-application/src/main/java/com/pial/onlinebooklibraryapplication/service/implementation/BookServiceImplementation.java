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
import com.pial.onlinebooklibraryapplication.exception.BookUnavailableException;
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
    private final BookRepository bookRepository;
    public BookServiceImplementation(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public BookDto createBook(BookDto book) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setStatus(AppConstants.STATUS_AVAILABLE);
        bookEntity.setDeleted(false);
        BookEntity storedBookDetails = bookRepository.save(bookEntity);
        return new ModelMapper().map(storedBookDetails,BookDto.class);
    }
    public List <BookDto> getAllBook() throws Exception{
        List<BookEntity> allBooks = bookRepository.findAllByDeletedFalse();
         return allBooks.stream()
                .map(bookEntity -> BookDto.builder()
                        .bookId(bookEntity.getBookId())
                        .title(bookEntity.getTitle())
                        .author(bookEntity.getAuthor())
                        .status(bookEntity.getStatus())
                        .build()
                )
                .collect(Collectors.toList());
    }
    public void deleteBook(BookDto bookDto) throws Exception {
        BookEntity bookEntity = bookRepository
                .findByBookIdAndDeletedFalse(bookDto.getBookId())
                .orElseThrow(()-> new BookIdNotFoundException(AppConstants.BOOK_NOTFOUND));
        if(AppConstants.STATUS_BORROWED.equals(bookEntity.getStatus()))
            throw new BookUnavailableException(AppConstants.BOOK_NOTDELETE);
        bookEntity.setDeleted(true);
        bookRepository.save(bookEntity);
    }
    public BookDto updateBook(BookDto bookDto) throws BookIdNotFoundException {
        BookEntity bookEntity = bookRepository.findByBookId(bookDto.getBookId())
                .filter(book -> !book.isDeleted())
                .orElseThrow(() -> new BookIdNotFoundException(AppConstants.BOOK_NOTFOUND));
        bookEntity.setTitle(bookDto.getTitle());
        bookEntity.setAuthor(bookDto.getAuthor());
        BookEntity storedBookDetails = bookRepository.save(bookEntity);
        return new ModelMapper().map(storedBookDetails,BookDto.class);
    }
}
