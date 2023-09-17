package com.whatbook.catalogservice.integeration;

import com.whatbook.catalogservice.TestConstants;
import com.whatbook.catalogservice.entities.Book;
import com.zaxxer.hikari.HikariDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.util.logging.LogManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integeration")
@Testcontainers
class BookMvcTest {
    private static Logger logger = LoggerFactory.getLogger(BookMvcTest.class);
    @Autowired
    private DataSource dataSource;
    @Autowired
    TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;

    @Container
    public static MySQLContainer<?>mySQLContainer = TestConstants.mySQLContainer;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }

    @AfterEach
    void tearDown() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }

    @Test
    void findByIsbnWhenExisting(){
        var bookIsbn = "1234561237";
        bookIsbn.hashCode();
        var book  = Book.of(bookIsbn,"title","author",12.90);
        Book res  = testRestTemplate.postForObject("http://localhost:"+ port+"/books",book,Book.class);
        Assertions.assertThat(res).isNotNull();
        logger.error(res.toString());
//        System.out.println(res);
//        Assertions.assertThat(res.getIsbn()).isEqualTo(book.getIsbn());
    }
}
