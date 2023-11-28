package org.bookshop;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BooksFileValidationService implements BooksFileValidation {

    @Override
    public List<Book> getUniqueBooksFromCSVFiles(List<MultipartFile> csvFiles) {
        return List.of();
    }
}
