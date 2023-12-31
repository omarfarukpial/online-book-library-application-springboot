package com.pial.onlinebooklibraryapplication.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookBorrowingDto {
    private Long borrowId;
    private Long userId;
    private Long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
}
