package com.gae.piaz.autogen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<E,P> {

    E create(E entity);

    E update(E entity);

    Page<E> read(E entity, Pageable pageable);
    E readOne(P primaryKey);

    void delete(P primaryKey);

}
