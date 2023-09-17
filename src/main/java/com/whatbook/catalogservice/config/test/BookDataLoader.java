package com.whatbook.catalogservice.config.test;

import com.whatbook.catalogservice.BookRepository;
import com.whatbook.catalogservice.entities.Book;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
@Component
@Profile("test")
public class BookDataLoader {
    private final BookRepository bookRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookDataLoader.class);

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        var book1 = Book.of("1234567891", "Northern Lights",
                "Lyra Silverstar", 9.90);
        var book2 =  Book.of("1234567892", "Polar Journey",
                "Iorek Polarson", 12.90);
        bookRepository.save(book1);
        logger.info(book1.toString());
        bookRepository.save(book2);
        logger.info(book2.toString());
    }
}
