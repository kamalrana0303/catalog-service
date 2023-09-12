package com.whatbook.catalogservice;

import com.whatbook.catalogservice.entities.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book,Long> {
    Optional<Book> findByIsbn(String isbn);
    Iterable<Book> findAll();
    boolean existsByIsbn(String isbn);
    Book save(Book book);
    void deleteByIsbn(String isbn);
}
