package org.bookshop.cart;

import java.util.List;

public record Cart(List<CartDetails> items)  {
}
