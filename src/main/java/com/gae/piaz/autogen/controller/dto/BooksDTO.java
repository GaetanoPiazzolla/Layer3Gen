package com.gae.piaz.autogen.controller.dto;

import lombok.Data;

@Data
public class BooksDTO {

    private Integer bookId;

    private String title;

    private String author;

    private String isbn;

    private Integer year;

}