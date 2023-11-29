package org.bookshop;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("admin")
public class AdminController {

    private final BooksFileValidation fileValidator;
    private final BookService bookService;

    public AdminController(final BooksFileValidation fileValidator, final BookService bookService) {
        this.fileValidator = fileValidator;
        this.bookService = bookService;
    }

    @PostMapping("loadBooks")
    public ResponseEntity<LoadBooksResponse> loadBooks(@RequestParam("file") MultipartFile file) throws Exception {
        List<Book> uniqueBooks = this.fileValidator.getUniqueBooksFromCSVFiles(file);
        int successSaveCount = bookService.loadBooks(uniqueBooks);
        return ResponseEntity.ok(new LoadBooksResponse(successSaveCount, "Books load successful"));
    }

}
