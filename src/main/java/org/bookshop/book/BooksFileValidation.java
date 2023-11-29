package org.bookshop.book;

import org.bookshop.book.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface BooksFileValidation {
    public List<Book> getUniqueBooksFromCSVFiles(MultipartFile csvFile) throws Exception;
}
