package org.bookshop;


public record Cart(String id, String userId, String bookId, Integer qty) {
}