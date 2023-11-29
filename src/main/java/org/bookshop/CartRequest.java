package org.bookshop;

public record CartRequest(String bookId,  String userId, Integer qty) {
}
