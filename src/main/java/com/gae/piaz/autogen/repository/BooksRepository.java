package com.gae.piaz.autogen.repository;

import com.gae.piaz.autogen.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Books, Integer> {

}
