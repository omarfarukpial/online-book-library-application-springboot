package com.pial.onlinebooklibraryapplication.repository;

import com.pial.onlinebooklibraryapplication.dto.BookReviewDto;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Long> {

    List<BookReviewDto> findAllBybookEntity(BookEntity bookEntity) throws Exception;

    List<BookReviewEntity> findAllByBookEntity(BookEntity bookEntity) throws Exception;

    BookReviewEntity findByReviewId(Long reviewId) throws Exception;
}
