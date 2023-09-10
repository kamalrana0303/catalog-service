package com.whatbook.catalogservice.entities;


import lombok.*;
import org.springframework.data.annotation.Version;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="books")
public class Book extends AuditId{
    @NotBlank(message = "The book isbn must be defined")
    @Pattern(regexp = "^([0-9]{10}|[0-9]{13})",
    message = "the isbn format must be valid")
    String isbn;
    @NotBlank(message = "the book title must be defined")
    String title;
    @NotBlank(message = "the book author must be defined")
    String author;
    @NotNull(message = "the book price must be defined")
    @Positive(message = "the book price must be greater than zero.")
    Double price;
    @Version
    int version;

    public static Book of(String isbn, String title, String author, Double price){
        return new Book(isbn,title,author,price,0);
    }
}
