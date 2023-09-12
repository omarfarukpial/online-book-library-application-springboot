package com.pial.onlinebooklibraryapplication.service.implementation;


import com.pial.onlinebooklibraryapplication.constants.AppConstants;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingInfoDto;
import com.pial.onlinebooklibraryapplication.dto.BookDto;
import com.pial.onlinebooklibraryapplication.dto.UserDto;
import com.pial.onlinebooklibraryapplication.entity.BookBorrowingEntity;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.BookReserveEntity;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import com.pial.onlinebooklibraryapplication.exception.*;
import com.pial.onlinebooklibraryapplication.repository.BookBorrowRepository;
import com.pial.onlinebooklibraryapplication.repository.BookRepository;
import com.pial.onlinebooklibraryapplication.repository.BookReserveRepository;
import com.pial.onlinebooklibraryapplication.repository.UserRepository;
import com.pial.onlinebooklibraryapplication.service.BookBorrowingService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookBorrowingServiceImplementation implements BookBorrowingService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookBorrowRepository bookBorrowRepository;
    private final BookReserveRepository bookReserveRepository;

    public BookBorrowingServiceImplementation(BookRepository bookRepository, UserRepository userRepository, BookBorrowRepository bookBorrowRepository, BookReserveRepository bookReserveRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookBorrowRepository = bookBorrowRepository;
        this.bookReserveRepository = bookReserveRepository;
    }
    public BookBorrowingDto bookBorrowing(Long bookId) throws Exception {
        UserEntity userEntity = getCurrentUser();
        BookEntity bookEntity = getBookById(bookId);
        if (Objects.equals(bookEntity.getStatus(), AppConstants.STATUS_BORROWED))
            throw new BookUnavailableException(AppConstants.BOOK_UNAVAILABLE);
        BookBorrowingEntity bookBorrowingEntity = new BookBorrowingEntity();
        bookBorrowingEntity.setBookEntity(bookEntity);
        bookBorrowingEntity.setUserEntity(userEntity);
        bookBorrowingEntity.setBorrowDate(LocalDate.now());
        bookBorrowingEntity.setDueDate(LocalDate.now().plusDays(14));
        bookEntity.setStatus(AppConstants.STATUS_BORROWED);
        BookBorrowingEntity storeBorrowDetails = bookBorrowRepository.save(bookBorrowingEntity);
        return new ModelMapper().map(storeBorrowDetails, BookBorrowingDto.class);
    }
    public BookBorrowingDto bookReturning(Long bookId) throws Exception{
        UserEntity userEntity = getCurrentUser();
        BookEntity bookEntity = getBookById(bookId);
        BookBorrowingEntity bookBorrowingEntity = bookBorrowRepository
                .findByUserEntityAndBookEntityAndReturnDateIsNull(userEntity,bookEntity)
                .orElseThrow(()-> new BookNotBorrowedException(AppConstants.BOOK_NOTBORROWED));
        bookEntity.setStatus(AppConstants.STATUS_AVAILABLE);
        bookBorrowingEntity.setReturnDate(LocalDate.now());
        bookReserveRepository.findAllByBookEntityAndStatus(bookEntity, AppConstants.STATUS_PENDING)
                .forEach(reserveEntity -> reserveEntity.setStatus(AppConstants.STATUS_AVAILABLE));
        BookBorrowingEntity storeReturnDetails = bookBorrowRepository.save(bookBorrowingEntity);
        return new ModelMapper().map(storeReturnDetails, BookBorrowingDto.class);
    }
    public List<BookDto> getAllBookByUser(Long userId) throws Exception {
        if (!getCurrentUser().getUserId().equals(userId) && getCurrentUser().getRole().equals(AppConstants.ROLE_CUSTOMER)) {
            throw new NotAuthorizedException(AppConstants.USER_UNAUTHORIZED);
        }
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserIdNotFoundException(AppConstants.USER_NOTFOUND));
        return bookBorrowRepository.findAllByUserEntity(userEntity).stream()
                .map(bookBorrowingEntity -> new ModelMapper().map(bookBorrowingEntity.getBookEntity(), BookDto.class))
                .toList();
    }
    public List<BookDto> getAllBorrowedBookByUser(Long userId) throws Exception {
        if (!getCurrentUser().getUserId().equals(userId) && getCurrentUser().getRole().equals(AppConstants.ROLE_CUSTOMER)) {
            throw new NotAuthorizedException(AppConstants.USER_UNAUTHORIZED);
        }
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserIdNotFoundException(AppConstants.USER_NOTFOUND));
        List<BookBorrowingEntity> bookBorrowings = bookBorrowRepository.findAllByUserEntityAndReturnDateIsNull(userEntity);
        return bookBorrowings.stream()
                .map(bookBorrowingEntity -> new ModelMapper().map(bookBorrowingEntity.getBookEntity(), BookDto.class))
                .collect(Collectors.toList());
    }
    public List<BookBorrowingInfoDto> getUserAllHistory(Long userId) throws Exception {
        if (!getCurrentUser().getUserId().equals(userId) && getCurrentUser().getRole().equals(AppConstants.ROLE_CUSTOMER)) {
            throw new NotAuthorizedException(AppConstants.USER_UNAUTHORIZED);
        }
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(()->new UserIdNotFoundException(AppConstants.USER_NOTFOUND));
        List<BookBorrowingEntity> bookBorrowings = bookBorrowRepository.findAllByUserEntity(userEntity);
        return bookBorrowings.stream()
                .map(bookBorrowingEntity -> BookBorrowingInfoDto.builder()
                        .borrowId(bookBorrowingEntity.getBorrowId())
                        .bookEntity(bookBorrowingEntity.getBookEntity())
                        .borrowDate(bookBorrowingEntity.getBorrowDate())
                        .dueDate(bookBorrowingEntity.getDueDate())
                        .returnDate(bookBorrowingEntity.getReturnDate())
                        .build()
                )
                .collect(Collectors.toList());
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
