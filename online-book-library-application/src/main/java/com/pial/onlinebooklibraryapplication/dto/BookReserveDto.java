package com.pial.onlinebooklibraryapplication.dto;

import com.pial.onlinebooklibraryapplication.entity.BookEntity;
import com.pial.onlinebooklibraryapplication.entity.UserEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReserveDto {
    private Long reserveId;
    private Long userId;
    private Long bookId;
    private String status;
    private LocalDate reserveDate;
}
