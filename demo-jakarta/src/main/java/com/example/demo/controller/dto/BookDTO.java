package com.example.demo.controller.dto;

import lombok.Data;

/**
* Generated by Springboot-3layer-Generator at Feb 22, 2023, 1:38:03 PM
*/
@Data
public class BookDTO {

    private long serialVersionUID;
    private java.lang.Integer bookId;
    private java.lang.String author;
    private java.lang.String isbn;
    private java.lang.String title;
    private java.lang.Integer year;
    private java.util.List orders;

}
