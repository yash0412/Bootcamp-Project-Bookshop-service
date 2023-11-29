package org.bookshop;

import java.util.List;

public record CartRequest(List<CartItem> cartItems) {
}
