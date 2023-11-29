package org.bookshop.cart;


public record Cart(String id, String userId, String bookId, Integer qty) {
}