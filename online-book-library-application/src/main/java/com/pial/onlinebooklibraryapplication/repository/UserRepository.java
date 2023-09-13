package com.pial.onlinebooklibraryapplication.repository;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserId(Long userId);
}
