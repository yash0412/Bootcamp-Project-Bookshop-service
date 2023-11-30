package org.bookshop.cart;

public record BookWithQuantity(String bookId, String bookName, Integer availableQuantity) {
}
