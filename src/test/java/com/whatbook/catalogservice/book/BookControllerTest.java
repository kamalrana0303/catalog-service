package com.whatbook.catalogservice.book;

import com.whatbook.catalogservice.entities.Book;
import com.whatbook.catalogservice.exceptions.BookAlreadyExistsException;
import com.whatbook.catalogservice.exceptions.BookNotFoundException;
import com.whatbook.catalogservice.resources.BookResource;
import com.whatbook.catalogservice.services.BookService;
import net.joshka.junit.json.params.JsonFileSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.json.JsonObject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookResource.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception{
        String isbn = "73737313940";
        given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException.class);
        mockMvc.perform(get("/books/"+isbn))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetBookExistingThenShouldReturn200() throws Exception {
        String isbn = "73737313940";
        given(bookService.viewBookDetails(isbn)).willReturn(Book.of(isbn,"title","author",12.0));
        mockMvc.perform(get("/books/"+isbn)).andExpect(status().isOk());
    }

    @Test
    void whenPostBookAlreadyExistThenShouldReturn422() throws Exception{
        given(bookService.addBookToCatalog(any(Book.class)))
                .willThrow(BookAlreadyExistsException.class);
        mockMvc.perform(post("/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content( "{\"isbn\":\"0123456789\",\"title\":\"title\",\"author\":\"author\",\"price\":12.0}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @ParameterizedTest
    @JsonFileSource(resources = {"/book.json"})
    void whenPostBookDoesnotExistThenShouldReturn201(JsonObject json) throws Exception {
        given(bookService.addBookToCatalog(any(Book.class))).willReturn(any(Book.class));
        mockMvc.perform(post("/books")
                .content(json.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
