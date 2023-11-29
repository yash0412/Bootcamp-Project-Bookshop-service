package org.bookshop.cart;


public record Item(String id, String userId, String bookId, Integer qty) {
}