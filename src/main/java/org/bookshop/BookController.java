package org.bookshop;


import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
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

    @PostMapping("booksByIds")
    public ResponseEntity<Books> getBookList(@RequestBody ListBookRequest request) {
        List<Book> allBooks =bookService.getBooks(request.bookIdList());
        return ResponseEntity.ok(new Books(allBooks));
    }
}
