package org.bookshop.book;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadFileException extends Exception {
    public BadFileException(String reason) {
        super(reason);
    }
}
