package org.bookshop;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getCartItems(String uuid) {
        return cartRepository.findAll()
                .stream()
                .filter(cartEntity -> cartEntity.userId.equals(uuid)).
                map(cartEntity -> new Cart(cartEntity.id, cartEntity.userId, cartEntity.bookId, cartEntity.qty))
                .toList();
    }
}
