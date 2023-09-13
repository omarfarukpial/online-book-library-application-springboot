package com.pial.onlinebooklibraryapplication.service.implementation;

import com.pial.onlinebooklibraryapplication.constants.AppConstants;
import com.pial.onlinebooklibraryapplication.dto.BookBorrowingDto;
import com.pial.onlinebooklibraryapplication.dto.BookReviewDto;
import com.pial.onlinebooklibraryapplication.entity.BookBorrowingEntity;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.BookReviewEntity;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import com.pial.onlinebooklibraryapplication.exception.BookIdNotFoundException;
import com.pial.onlinebooklibraryapplication.exception.NotAuthorizedException;
import com.pial.onlinebooklibraryapplication.exception.ReviewAlreadyExists;
import com.pial.onlinebooklibraryapplication.exception.ReviewIdNotFoundException;
import com.pial.onlinebooklibraryapplication.repository.BookRepository;
import com.pial.onlinebooklibraryapplication.repository.BookReviewRepository;
import com.pial.onlinebooklibraryapplication.repository.UserRepository;
import com.pial.onlinebooklibraryapplication.service.BookReviewService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional

public class BookReviewServiceImplementation implements BookReviewService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookReviewRepository bookReviewRepository;
    public BookReviewServiceImplementation(UserRepository userRepository, BookRepository bookRepository, BookReviewRepository bookReviewRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookReviewRepository = bookReviewRepository;
    }
    public BookReviewDto createBookReview(Long bookId, BookReviewDto bookReviewDto) throws Exception {
        UserEntity userEntity = getCurrentUser();
        BookEntity bookEntity = getBookById(bookId);
        if (bookReviewRepository
                .findByUserEntityAndBookEntity(userEntity, bookEntity)
                .isPresent()
        ) throw new ReviewAlreadyExists(AppConstants.BOOK_REVIEWED_BEFORE);
        BookReviewEntity bookReviewEntity = new BookReviewEntity();
        bookReviewEntity.setBookEntity(bookEntity);
        bookReviewEntity.setUserEntity(userEntity);
        bookReviewEntity.setReview(bookReviewDto.getReview());
        bookReviewEntity.setRating(bookReviewDto.getRating());
        bookReviewEntity.setDate(LocalDate.now());
        BookReviewEntity storeReviewDetails = bookReviewRepository.save(bookReviewEntity);
        return new ModelMapper().map(storeReviewDetails, BookReviewDto.class);
    }
    public List<BookReviewDto> allBookReview(Long bookId) throws Exception {
        BookEntity bookEntity = getBookById(bookId);
        List<BookReviewEntity> bookReviews = bookReviewRepository.findAllByBookEntityAndDeletedFalse(bookEntity);
        return bookReviews.stream()
                .map(reviewEntity -> new ModelMapper().map(reviewEntity, BookReviewDto.class))
                .collect(Collectors.toList());
    }
    public void deleteReview(Long bookId, Long reviewId) throws Exception {
        UserEntity userEntity = getCurrentUser();
        BookEntity bookEntity = getBookById(bookId);
        BookReviewEntity bookReview = bookReviewRepository
                .findByReviewIdAndBookEntityAndUserEntityAndDeletedFalse(reviewId, bookEntity, userEntity)
                .orElseThrow(() -> new ReviewIdNotFoundException(AppConstants.BOOK_REVIEWED_NOTFOUND));
        bookReview.setDeleted(true);
        bookReviewRepository.save(bookReview);
    }
    public BookReviewDto updateReview(Long bookId, Long reviewId, BookReviewDto bookReviewDto) throws Exception {
        UserEntity userEntity = getCurrentUser();
        BookEntity bookEntity = getBookById(bookId);
        BookReviewEntity bookReview = bookReviewRepository
                .findByReviewIdAndBookEntityAndUserEntityAndDeletedFalse(reviewId, bookEntity, userEntity)
                .orElseThrow(() -> new ReviewIdNotFoundException(AppConstants.BOOK_REVIEWED_NOTFOUND));
        bookReview.setRating(bookReviewDto.getRating());
        bookReview.setReview(bookReviewDto.getReview());
        bookReview.setDate(LocalDate.now());
        bookReviewRepository.save(bookReview);
        return new ModelMapper().map(bookReview, BookReviewDto.class);
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
