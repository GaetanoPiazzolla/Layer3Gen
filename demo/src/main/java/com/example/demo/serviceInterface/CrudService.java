package com.example.demo.serviceInterface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

/**
* Generated by Springboot-3layer-Generator at Feb 21, 2023, 7:59:17 AM
*/
public interface CrudService<E, P> {

    E create(E entity);

    E update(E entity);

    Page<E> read(Pageable pageable);

    Optional<E> readOne(P primaryKey);

    void delete(P primaryKey);

}