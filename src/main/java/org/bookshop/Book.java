package org.bookshop;

public record Book(String id, String isbn, String title, String description,
                   String author, String publicationYear, double price,
                   int quantity, String imageUrl, String shortImageUrl, double averageRating) {
}
