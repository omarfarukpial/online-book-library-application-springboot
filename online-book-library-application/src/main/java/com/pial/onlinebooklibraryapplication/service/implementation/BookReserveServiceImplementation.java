package com.pial.onlinebooklibraryapplication.service.implementation;

import com.pial.onlinebooklibraryapplication.constants.AppConstants;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookReserveDto;
import com.pial.onlinebooklibraryapplication.dto.BookReviewDto;
import com.pial.onlinebooklibraryapplication.entity.BookBorrowingEntity;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.BookReserveEntity;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import com.pial.onlinebooklibraryapplication.exception.*;
import com.pial.onlinebooklibraryapplication.repository.BookBorrowRepository;
import com.pial.onlinebooklibraryapplication.repository.BookRepository;
import com.pial.onlinebooklibraryapplication.repository.BookReserveRepository;
import com.pial.onlinebooklibraryapplication.repository.UserRepository;
import com.pial.onlinebooklibraryapplication.service.BookReserveService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
public class BookReserveServiceImplementation implements BookReserveService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookReserveRepository bookReserveRepository;
    private final BookBorrowRepository bookBorrowRepository;
    public BookReserveServiceImplementation(BookRepository bookRepository, UserRepository userRepository, BookReserveRepository bookReserveRepository, BookBorrowRepository bookBorrowRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookReserveRepository = bookReserveRepository;
        this.bookBorrowRepository = bookBorrowRepository;
    }

    public BookReserveDto reserveBook(Long bookId) throws Exception{
        UserEntity userEntity = getCurrentUser();
        BookEntity bookEntity = getBookById(bookId);
        if (AppConstants.STATUS_AVAILABLE.equals(bookEntity.getStatus()))
            throw new BookAlreadyExistsException(AppConstants.BOOK_AVAILABLE);
        if (bookReserveRepository
                .findByUserEntityAndBookEntityAndStatus(userEntity, bookEntity, AppConstants.STATUS_PENDING)
                .isPresent()
        ) throw new BookReservedBeforeException(AppConstants.BOOK_RESERVEDBYYOU);
        if (bookBorrowRepository
                .findByUserEntityAndBookEntityAndReturnDateIsNull(userEntity, bookEntity)
                .isPresent()
        ) throw new BookNotBorrowedException(AppConstants.BOOK_BORROWEDBYYOU);
        BookReserveEntity bookReserveEntity = new BookReserveEntity();
        bookReserveEntity.setBookEntity(bookEntity);
        bookReserveEntity.setUserEntity(userEntity);
        bookReserveEntity.setReserveDate(LocalDate.now());
        bookReserveEntity.setStatus(AppConstants.STATUS_PENDING);
        BookReserveEntity storeReserveDetails = bookReserveRepository.save(bookReserveEntity);
        return new ModelMapper().map(storeReserveDetails, BookReserveDto.class);
    }

    public BookReserveDto cancelReserveBook(Long bookId) throws Exception {
        UserEntity userEntity = getCurrentUser();
        BookEntity bookEntity = getBookById(bookId);
        BookReserveEntity bookCancelReserveEntity = bookReserveRepository
                .findByUserEntityAndBookEntityAndStatus(userEntity, bookEntity, AppConstants.STATUS_PENDING)
                .orElseThrow(()-> new BookNotReservedException(AppConstants.BOOK_RESERVATION_NOTFOUND));
        bookCancelReserveEntity.setStatus(AppConstants.STATUS_CANCEL);
        return new ModelMapper().map(bookCancelReserveEntity, BookReserveDto.class);
    }

    private UserEntity getCurrentUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new Exception(AppConstants.TOKEN_INVALID));
    }
    private BookEntity getBookById(Long bookId) throws Exception {
        return bookRepository.findByBookId(bookId)
                .filter(book -> !book.isDeleted())
                .orElseThrow(() -> new BookIdNotFoundException(AppConstants.BOOK_NOTFOUND));
    }

}
