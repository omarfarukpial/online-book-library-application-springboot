package com.pial.onlinebooklibraryapplication.service.implementation;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookReviewRepository bookReviewRepository;



    public BookReviewDto createBookReview(Long bookId, BookReviewDto bookReviewDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        Long userId = user.get().getUserId();

        UserEntity userEntity = userRepository.findByUserId(userId);
        BookEntity bookEntity = bookRepository.findByBookId(bookId);

        if (bookEntity == null || bookEntity.isDeleted()) throw new BookIdNotFoundException("Book does not exits!");

        ModelMapper modelMapper = new ModelMapper();

        BookReviewEntity bookReviewCheckEntity = bookReviewRepository.findByUserEntityAndBookEntity(userEntity, bookEntity);
        if (bookReviewCheckEntity != null) throw new ReviewAlreadyExists("You gave review already, you can update your review!");

        BookReviewEntity bookReviewEntity = new BookReviewEntity();

        bookReviewEntity.setBookEntity(bookEntity);
        bookReviewEntity.setUserEntity(userEntity);
        bookReviewEntity.setReview(bookReviewDto.getReview());
        bookReviewEntity.setRating(bookReviewDto.getRating());
        bookReviewEntity.setDate(LocalDate.now());


        BookReviewEntity storeReview = bookReviewRepository.save(bookReviewEntity);

        return modelMapper.map(storeReview, BookReviewDto.class);



    }

    public List<BookReviewDto> allBookReview(Long bookId) throws Exception {
        BookEntity bookEntity = bookRepository.findByBookId(bookId);
        if (bookEntity == null || bookEntity.isDeleted()) throw new BookIdNotFoundException("Book does not exists!");
        ModelMapper modelMapper = new ModelMapper();
        List<BookReviewEntity> bookReviews = bookReviewRepository.findAllByBookEntity(bookEntity);
        List<BookReviewDto> bookReviewDtos = bookReviews.stream()
                .map(reviewEntity -> modelMapper.map(reviewEntity, BookReviewDto.class))
                .collect(Collectors.toList());
        return bookReviewDtos;
    }


    public void deleteReview(Long bookId, Long reviewId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        Long userId = user.get().getUserId();

        BookEntity bookEntity = bookRepository.findByBookId(bookId);
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (bookEntity == null || bookEntity.isDeleted()) throw new BookIdNotFoundException("Book Id does not exists!");

        BookReviewEntity bookReview = bookReviewRepository.findByReviewIdAndBookEntityAndUserEntity(reviewId, bookEntity, userEntity);
        if (bookReview == null) throw new ReviewIdNotFoundException("This review does not exists!");
        bookReviewRepository.delete(bookReview);
    }

    public BookReviewDto updateReview(Long bookId, Long reviewId, BookReviewDto bookReviewDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        Long userId = user.get().getUserId();


        BookEntity bookEntity = bookRepository.findByBookId(bookId);
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (bookEntity == null || bookEntity.isDeleted()) throw new BookIdNotFoundException("Book Id does not exists!");


        BookReviewEntity bookReview = bookReviewRepository.findByReviewIdAndBookEntityAndUserEntity(reviewId, bookEntity, userEntity);
        if (bookReview == null) throw new ReviewIdNotFoundException("This review does not exists!");

        bookReview.setRating(bookReviewDto.getRating());
        bookReview.setReview(bookReviewDto.getReview());
        bookReview.setDate(LocalDate.now());

        bookReviewRepository.save(bookReview);
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(bookReview, BookReviewDto.class);
    }
}
