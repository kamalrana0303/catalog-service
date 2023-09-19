package com.whatbook.catalogservice.slice.book;

import com.whatbook.catalogservice.BookRepository;
import com.whatbook.catalogservice.TestConstants;
import com.whatbook.catalogservice.config.DataConfig;
import com.whatbook.catalogservice.config.test.TestAuditingConfiguration;
import com.whatbook.catalogservice.config.test.BookDataLoader;
import com.whatbook.catalogservice.entities.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
@DataJpaTest  //Identifies a test class that focuses on Spring Data JDBC components
@Import({DataConfig.class,BookDataLoader.class, TestConstants.class}) //Imports the data configuration (needed to enable auditing)
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE  //Disables the default behavior of relying on an embedded test database since we want to use Testcontainers
)
public class BookRepositoryTest extends TestConstants {
    @Autowired
    private BookRepository bookRepository;

    @Container
    public static MySQLContainer<?> mySQLContainer =  TestConstants.mySQLContainer;

    @DynamicPropertySource
    public static void registerMysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }

    @Test
    void findByIsbnWhenExisting(){
        var bookIsbn = "1234567891";
        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);
        Assertions.assertThat(actualBook).isPresent();
        Assertions.assertThat(actualBook.get().getIsbn()).isEqualTo(bookIsbn);
    }

    @Test
    void existsByIsbnWhenExisting(){
        var bookIsbn = "1234567891";
        boolean b = bookRepository.existsByIsbn(bookIsbn);
        Assertions.assertThat(b).isTrue();
    }
    @Test
    void deleteByIsbnWhenExisting(){
        var bookIsbn = "1234567891";
        bookRepository.deleteByIsbn(bookIsbn);
        Optional<Book> byIsbn = bookRepository.findByIsbn(bookIsbn);
        Assertions.assertThat(byIsbn).isEmpty();
    }
}