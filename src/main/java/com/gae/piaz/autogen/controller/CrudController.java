package com.gae.piaz.autogen.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

public interface CrudController<O,P>{

    @PostMapping
    ResponseEntity<O> create(O object);

    @PutMapping
    ResponseEntity<O> update(O object);

    @PostMapping("find")
    ResponseEntity<Page<O>> read(O object, Integer page, Integer size);
    @GetMapping("{id}")
    ResponseEntity<O> readOne(P primaryKey);

    @DeleteMapping
    void delete(P primaryKey);

}
