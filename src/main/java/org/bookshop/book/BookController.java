package org.bookshop.book;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
public class BookController {

    private BookService bookService;

    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("books")
    public ResponseEntity<Books> allBooks(@RequestParam(value = "query", required = false) String query) {
        if (query == null || query.isEmpty()) {
            List<Book> allBooks = bookService.getAllBooks();
            return ResponseEntity.ok(new Books(allBooks));
        }
        if (query.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter Valid Request");
        }
        List<Book> searchedBook = bookService.searchBook(query);
        return ResponseEntity.ok(new Books(searchedBook));
    }


}
