package org.bookshop.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class OrderCreationFailedException extends Exception {
    public OrderCreationFailedException(String failedToCreateOrder) {
        super(failedToCreateOrder);
    }
}
