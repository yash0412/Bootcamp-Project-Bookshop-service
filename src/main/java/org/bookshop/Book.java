package org.bookshop;

public record Book(String id, String title, String author, double price, int quantity, String imageUrl) {
}
