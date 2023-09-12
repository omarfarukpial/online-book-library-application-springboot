package com.pial.onlinebooklibraryapplication.repository;

import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.BookReserveEntity;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReserveRepository extends JpaRepository<BookReserveEntity, Long> {

    List<BookReserveEntity> findAllByBookEntityAndStatus(BookEntity bookEntity, String pending);
    BookReserveEntity findByUserEntityAndBookEntityAndStatus(UserEntity userEntity, BookEntity bookEntity, String pending);
}
