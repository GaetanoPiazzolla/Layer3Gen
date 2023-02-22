package com.example.demo.serviceInterface.mapper;

import com.example.demo.model.Book;
import com.example.demo.controller.dto.BookDTO;
import org.mapstruct.Mapper;

/**
* Generated by Springboot-3layer-Generator at Feb 22, 2023, 1:38:03 PM
*/
@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toEntity(BookDTO dto);

    BookDTO toDto(Book entity);

}