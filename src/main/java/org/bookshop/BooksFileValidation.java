package org.bookshop;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface BooksFileValidation {
    public List<Book> getUniqueBooksFromCSVFiles(List<MultipartFile> csvFiles);
}
