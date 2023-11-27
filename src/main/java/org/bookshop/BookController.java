package org.bookshop;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    private BookService bookService;

    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("books")
    public ResponseEntity<Books> allBooks(){
        List<Book> allBooks = bookService.getAllBooks();
        return ResponseEntity.ok(new Books(allBooks));
    }
}
