package com.pial.onlinebooklibraryapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {

    private long id;
    private String title;
    private String author;
    private String bookId;
    private String status;


}
