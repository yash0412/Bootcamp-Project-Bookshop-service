package org.bookshop.cart;

import java.util.List;

public record CartRequest(List<CartItem> cartItems) {
}
