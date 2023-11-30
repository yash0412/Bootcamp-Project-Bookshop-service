package org.bookshop.cart;

import org.bookshop.book.Book;

public record CartDetails(Integer qty, Book book) {
}
