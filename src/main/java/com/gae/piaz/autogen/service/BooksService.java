package com.gae.piaz.autogen.service;

import com.gae.piaz.autogen.model.Books;
import com.gae.piaz.autogen.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BooksService implements CrudService<Books,Integer> {

    @Autowired
    private BooksRepository repository;

    @Override
    public Books create(Books entity) {
        return repository.save(entity);
    }

    @Override
    public Books update(Books entity) {
        return repository.save(entity);
    }

    @Override
    public Page<Books> read(Books entity, Pageable pageable) {
        Example<Books> booksExample = Example.of(entity);
        return repository.findAll(booksExample,pageable);
    }

    @Override
    public Books readOne(Integer primaryKey) {
        return repository.getOne(primaryKey);
    }

    @Override
    public void delete(Integer primaryKey) {
        repository.deleteById(primaryKey);
    }
}
