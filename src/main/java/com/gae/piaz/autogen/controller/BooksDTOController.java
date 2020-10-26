package com.gae.piaz.autogen.controller;

import com.gae.piaz.autogen.controller.dto.BooksDTO;
import com.gae.piaz.autogen.mapper.BooksMapper;
import com.gae.piaz.autogen.model.Books;
import com.gae.piaz.autogen.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books-dto/")
public class BooksDTOController implements CrudController<BooksDTO,Integer>{

    @Autowired
    private BooksService booksService;

    @Autowired
    private BooksMapper booksMapper;

    @Override
    public ResponseEntity<BooksDTO> create(@RequestBody BooksDTO dto) {
        Books entity = booksMapper.toEntity(dto);
        entity = booksService.create(entity);
        return ResponseEntity.ok(booksMapper.toDto(entity));
    }

    @Override
    public ResponseEntity<BooksDTO> update(@RequestBody BooksDTO dto) {
        Books entity = booksMapper.toEntity(dto);
        entity = booksService.update(entity);
        return ResponseEntity.ok(booksMapper.toDto(entity));
    }

    @Override
    public ResponseEntity<Page<BooksDTO>> read(
            @RequestBody BooksDTO dto,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Books entity = booksMapper.toEntity(dto);
        Page<BooksDTO> pages = booksService.read(entity, pageable).map(booksMapper::toDto);
        return ResponseEntity.ok(pages);
    }

    @Override
    public ResponseEntity<BooksDTO> readOne(@PathVariable("id") Integer primaryKey) {
        Books entity = booksService.readOne(primaryKey);
        return ResponseEntity.ok(booksMapper.toDto(entity));
    }

    @Override
    public void delete(Integer primaryKey) {
        booksService.delete(primaryKey);
    }
}
