package com.whatbook.catalogservice.services;

import com.whatbook.catalogservice.BookRepository;
import com.whatbook.catalogservice.entities.Book;
import com.whatbook.catalogservice.exceptions.BookAlreadyExistsException;
import com.whatbook.catalogservice.exceptions.BookNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImp implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImp(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Iterable<Book> viewBookList(){
        return bookRepository.findAll();
    }

    @Override
    public Book viewBookDetails(String isbn){
        return bookRepository.findByIsbn(isbn).orElseThrow(()->new BookNotFoundException(isbn));
    }

    @Override
    public Book addBookToCatalog(Book book){
        if(bookRepository.existsByIsbn(book.getIsbn()) == 1){
            throw new BookAlreadyExistsException(book.getIsbn());
        }
        return bookRepository.save(book);
    }

    @Override
    public void removeBookFromCatalog(String isbn){
        bookRepository.deleteByIsbn(isbn);
    }

    @Override
    public Book editBookDetails(String isbn, Book book){
        return bookRepository.findByIsbn(isbn)
                .map(existingBook-> {
                    var bookToUpdate = Book.of(existingBook.getIsbn(),book.getTitle(),book.getAuthor(),book.getPrice());
                    bookToUpdate.setVersion(existingBook.getVersion());
                    bookToUpdate.setCreatedDate(existingBook.getCreatedDate());
                    bookToUpdate.setLastModifiedDate(existingBook.getLastModifiedDate());
                    return bookRepository.save(bookToUpdate);
                })
                .orElseGet(()->addBookToCatalog(book));
    }

}
