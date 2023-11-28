package org.bookshop;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepo) {

        this.bookRepository = bookRepo;
    }
    public List<Book> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookEntity -> new Book(bookEntity.Id,
                        bookEntity.title,
                        bookEntity.author,
                        bookEntity.price,
                        bookEntity.quantity,
                        bookEntity.imageurl))
                .toList();
    }
}
