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
@Table(name = "book_reserving")
public class BookReserveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reserveId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private BookEntity bookEntity;

    private String status;

    private LocalDate reserveDate;



}
