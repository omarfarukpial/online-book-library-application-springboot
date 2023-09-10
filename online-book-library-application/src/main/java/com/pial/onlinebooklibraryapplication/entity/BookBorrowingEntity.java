package com.pial.onlinebooklibraryapplication.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "book_borrowing")
public class BookBorrowingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private BookEntity bookEntity;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;




}
