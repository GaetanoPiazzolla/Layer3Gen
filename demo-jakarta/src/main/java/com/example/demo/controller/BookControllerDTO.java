package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.controller.dto.BookDTO;
import com.example.demo.serviceInterface.BookService;
import com.example.demo.serviceInterface.mapper.BookMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
* Generated by Springboot-3layer-Generator at Feb 21, 2023, 8:01:16 AM
*/
@RestController
@RequestMapping("/book-dto/")
public class BookControllerDTO implements CrudController<BookDTO,java.lang.Integer>{

    @Autowired
    private BookService service;

    @Autowired
    private BookMapper mapper;

    @Override
    public ResponseEntity<BookDTO> create(@RequestBody BookDTO dto) {
       Book entity = mapper.toEntity(dto);
       entity = service.create(entity);
       return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<BookDTO> update(@RequestBody BookDTO dto) {
      Book entity = mapper.toEntity(dto);
       entity = service.update(entity);
       return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<Page<BookDTO>> read(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<BookDTO> pages = service.read(pageable).map(mapper::toDto);
        return ResponseEntity.ok(pages);
    }

    @Override
    public ResponseEntity<BookDTO> readOne(@PathVariable("id") java.lang.Integer primaryKey) {
         Optional<Book> entity = service.readOne(primaryKey);
         return entity.map(e -> ResponseEntity.ok(mapper.toDto(e))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public void delete(java.lang.Integer primaryKey) {
        service.delete(primaryKey);
    }
}