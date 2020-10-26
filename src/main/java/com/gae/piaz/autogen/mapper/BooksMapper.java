package com.gae.piaz.autogen.mapper;

import com.gae.piaz.autogen.controller.dto.BooksDTO;
import com.gae.piaz.autogen.model.Books;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BooksMapper {

    Books toEntity(BooksDTO dto);

    BooksDTO toDto(Books entity);

}
