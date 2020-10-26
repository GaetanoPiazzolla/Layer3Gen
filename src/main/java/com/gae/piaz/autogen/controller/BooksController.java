package com.gae.piaz.autogen.controller;

import com.gae.piaz.autogen.model.Books;
import com.gae.piaz.autogen.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books/")
public class BooksController implements CrudController<Books,Integer>{

    @Autowired
    private BooksService booksService;

    @Override
    public ResponseEntity<Books> create(@RequestBody Books entity) {
        return ResponseEntity.ok(booksService.create(entity));
    }

    @Override
    public ResponseEntity<Books> update(@RequestBody Books entity) {
        return ResponseEntity.ok(booksService.update(entity));
    }

    @Override
    public ResponseEntity<Page<Books>> read(
            @RequestBody Books entity,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(booksService.read(entity,pageable));
    }

    @Override
    public ResponseEntity<Books> readOne(@PathVariable("id") Integer primaryKey) {
        return ResponseEntity.ok(booksService.readOne(primaryKey));
    }

    @Override
    public void delete(Integer primaryKey) {
        booksService.delete(primaryKey);
    }
}
