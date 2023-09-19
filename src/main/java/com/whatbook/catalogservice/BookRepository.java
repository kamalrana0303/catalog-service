package com.whatbook.catalogservice;

import com.whatbook.catalogservice.entities.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book,Long> {
    @Query(value = "select id,created_date,last_modified_date,isbn,author,price,title,version from books where isbn =:isbn",nativeQuery = true)
    Optional<Book> findByIsbn(@Param("isbn") String isbn);
    @Query(value = "select id,created_date,last_modified_date,isbn,author,price,title,version from books",nativeQuery = true)
    List<Book> findAll();
    @Query(value = "SELECT CASE WHEN COUNT(id) > 0 THEN 1 ELSE  0 END FROM books WHERE isbn =:isbn",nativeQuery = true)
    int existsByIsbn(@Param("isbn") String isbn);
    Book save(Book book);
    @Modifying
    @Query(value = "delete from books where isbn =:isbn",nativeQuery = true)
    void deleteByIsbn(@Param("isbn") String isbn);
}
