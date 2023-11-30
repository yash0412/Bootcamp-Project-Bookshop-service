package org.bookshop.cart;

import java.util.List;

public record CheckoutValidationResponse(String error, List<BookWithQuantity> errorDetails) {
}
