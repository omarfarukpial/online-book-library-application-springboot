package com.pial.onlinebooklibraryapplication.repository;
import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional <BookEntity> findByBookId(Long bookId);
    Optional<BookEntity> findByBookIdAndDeletedFalse(Long bookId);
    List<BookEntity> findAllByDeletedFalse();
}
