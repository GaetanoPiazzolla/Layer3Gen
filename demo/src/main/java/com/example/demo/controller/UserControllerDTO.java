package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.service.UserService;
import com.example.demo.service.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
* Generated by Springboot-3layer-Generator at Feb 26, 2023, 4:26:01 PM
*/
@RestController
@RequestMapping("/user-dto/")
public class UserControllerDTO implements CrudController<UserDTO,java.lang.Integer>{

    @Autowired
    private UserService service;

    @Autowired
    private UserMapper mapper;

    @Override
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
       User entity = mapper.toEntity(dto);
       entity = service.create(entity);
       return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO dto) {
      User entity = mapper.toEntity(dto);
       entity = service.update(entity);
       return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<Page<UserDTO>> find(
            @RequestBody UserDTO dto,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        User entity = mapper.toEntity(dto);
        Page<UserDTO> pages = service.find(entity, pageable).map(mapper::toDto);
        return ResponseEntity.ok(pages);
    }

    @Override
    public ResponseEntity<UserDTO> getOne(@PathVariable("id") java.lang.Integer primaryKey) {
         Optional<User> entity = service.getOne(primaryKey);
         return entity.map(e -> ResponseEntity.ok(mapper.toDto(e))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public void delete(java.lang.Integer primaryKey) {
        service.delete(primaryKey);
    }
}