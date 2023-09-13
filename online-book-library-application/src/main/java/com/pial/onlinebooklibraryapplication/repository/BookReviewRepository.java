package com.pial.onlinebooklibraryapplication.repository;

import com.pial.onlinebooklibraryapplication.dto.BookReviewDto;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.BookReviewEntity;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Long> {
    Optional<BookReviewEntity> findByUserEntityAndBookEntity(UserEntity userEntity, BookEntity bookEntity);
    List<BookReviewEntity> findAllByBookEntityAndDeletedFalse(BookEntity bookEntity);
    Optional<BookReviewEntity> findByReviewIdAndBookEntityAndUserEntityAndDeletedFalse(Long reviewId, BookEntity bookEntity, UserEntity userEntity);
}
