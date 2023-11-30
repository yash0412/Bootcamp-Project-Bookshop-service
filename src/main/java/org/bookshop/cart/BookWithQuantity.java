package org.bookshop.cart;

public record BookWithQuantity(String id, String title, Integer availableQuantity) {
}
