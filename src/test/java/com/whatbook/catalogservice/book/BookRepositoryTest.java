package com.whatbook.catalogservice.book;

import com.whatbook.catalogservice.BookRepository;
import com.whatbook.catalogservice.config.DataConfig;
import com.whatbook.catalogservice.entities.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJdbcTest  //Identifies a test class that focuses on Spring Data JDBC components
@Import(DataConfig.class) //Imports the data configuration (needed to enable auditing)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE  //Disables the default behavior of relying on an embedded test database since we want to use Testcontainers
)
@ActiveProfiles("integeration") //Enables the “integration” profile to load configuration from application-integration.yml
public class BookRepositoryTest {
    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;  //A lower-level object to interact with the database
    @Autowired
    private BookRepository bookRepository;

    @Test
    void findByIsbnWhenExisting(){
        var bookIsbn = "1234561237";
        var book  = Book.of(bookIsbn,"title","author",12.90);
        jdbcAggregateTemplate.insert(book);
        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);
        Assertions.assertThat(actualBook).isPresent();
        Assertions.assertThat(actualBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }
}
