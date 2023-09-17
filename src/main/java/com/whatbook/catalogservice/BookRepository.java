package com.whatbook.catalogservice;

import com.whatbook.catalogservice.entities.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book,Long> {
    @Query("select b from Book b  where b.isbn =:isbn")
    Optional<Book> findByIsbn(@Param("isbn") String isbn);
    @Query("select b from Book b")
    Iterable<Book> findAll();
    @Query("SELECT COUNT(b) > 0 FROM Book b WHERE b.isbn =:isbn")
    boolean existsByIsbn(@Param("isbn") String isbn);
    Book save(Book book);
    @Modifying
    @Query("delete from Book b where b.isbn =:isbn")
    void deleteByIsbn(@Param("isbn") String isbn);
}
