package com.example.demo.controller.dto;

import lombok.Data;

/**
* Generated by Springboot-3layer-Generator at Feb 26, 2023, 4:26:01 PM
*/
@Data
public class OrderDTO {

    private long serialVersionUID;
    private java.lang.Integer orderId;
    private java.lang.Integer quantity;
    private com.example.demo.model.Book book;
    private com.example.demo.model.User user;

}
