package com.pial.onlinebooklibraryapplication.repository;

import com.pial.onlinebooklibraryapplication.dto.BookDto;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    void deleteByBookId(Long bookId);

    boolean existsByBookId(Long bookId);

    BookEntity findByBookId(Long bookId);
}
