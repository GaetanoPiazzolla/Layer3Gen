package com.example.demo.service.mapper;

import com.example.demo.model.User;
import com.example.demo.controller.dto.UserDTO;
import org.mapstruct.Mapper;

/**
* Generated by Springboot-3layer-Generator at Jan 23, 2023, 12:19:32 PM
*/
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO dto);

    UserDTO toDto(User entity);

}