package org.bookshop.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidCartException extends Exception {
    InvalidCartException(String error) {
        super(error);
    }
}
