package org.bookshop;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("admin")
public class AdminController {

    private BooksFileValidation fileValidator;

    public AdminController(final BooksFileValidation fileValidator) {
        this.fileValidator = fileValidator;
    }

    @PostMapping("loadBooks")
    public void loadBooks(@RequestParam("files") MultipartFile[] files) {
        List<Book> books = this.fileValidator.getUniqueBooksFromCSVFiles(List.of(files));
    }
}
