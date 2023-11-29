package org.bookshop.cart;

public record CartItem(String bookId, String userId, Integer qty) {
}

