package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
* Generated by Springboot-3layer-Generator at Jan 23, 2023, 12:19:32 PM
*/
@RestController
@RequestMapping("/order/")
public class OrderController implements CrudController<Order,java.lang.Integer>{

    @Autowired
    private OrderService service;

    @Override
    public ResponseEntity<Order> create(@RequestBody Order entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @Override
    public ResponseEntity<Order> update(@RequestBody Order entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @Override
    public ResponseEntity<Page<Order>> read(
            @RequestBody Order entity,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(service.read(entity,pageable));
    }

    @Override
    public ResponseEntity<Order> readOne(@PathVariable("id") java.lang.Integer primaryKey) {
        return ResponseEntity.ok(service.readOne(primaryKey));
    }

    @Override
    public void delete(java.lang.Integer primaryKey) {
        service.delete(primaryKey);
    }
}