package com.whatbook.catalogservice.slice.book;

import com.whatbook.catalogservice.BookRepository;
import com.whatbook.catalogservice.config.DataConfig;
import com.whatbook.catalogservice.config.TestAuditingConfiguration;
import com.whatbook.catalogservice.entities.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;
@DataJpaTest  //Identifies a test class that focuses on Spring Data JDBC components
@Import(TestAuditingConfiguration.class) //Imports the data configuration (needed to enable auditing)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE  //Disables the default behavior of relying on an embedded test database since we want to use Testcontainers
)
@Testcontainers
@ActiveProfiles("test")
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Container
    public static MySQLContainer<?> mySQLContainer =  new MySQLContainer<>(DockerImageName.parse("mysql:8"))
            .withDatabaseName("projectDb")
            .withUsername("root")
            .withPassword("password")
            .waitingFor(Wait.forListeningPort())
            .withEnv("MYSQL_ROOT_HOST","%");

    @DynamicPropertySource
    static void registerMysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }

    @Test
    void findByIsbnWhenExisting(){
        var bookIsbn = "1234561237";
        var book  = Book.of(bookIsbn,"title","author",12.90);
        bookRepository.save(book);
        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);
        Assertions.assertThat(actualBook).isPresent();
        Assertions.assertThat(actualBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }
}