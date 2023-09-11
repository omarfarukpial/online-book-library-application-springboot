package com.pial.onlinebooklibraryapplication.repository;

import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.BookReserveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReserveRepository extends JpaRepository<BookReserveEntity, Long> {
    BookReserveEntity findByBookEntity(BookEntity bookEntity);

    List<BookReserveEntity> findAllByBookEntityAndDeletedFalse(BookEntity bookEntity);
}
