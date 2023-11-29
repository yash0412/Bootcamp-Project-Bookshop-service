package org.bookshop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getCartItems(String userId) {
        return cartRepository.findAll()
                .stream()
                .filter(cartEntity -> cartEntity.userId.equals(userId)).
                map(cartEntity -> new Cart(cartEntity.id, cartEntity.userId, cartEntity.bookId, cartEntity.qty))
                .toList();
    }

    public CartEntity createCartItem(String id, String bookId, String userId, Integer qty) {
        return cartRepository.saveAndFlush(
                new CartEntity(
                        id,
                        bookId,
                        userId,
                        qty
                )
        );
    }

    public void updateCartItem(String bookId, String userId, Integer qty) {
        List<CartEntity> cartEntity = cartRepository.findCartEntitiesByBookIdAndUserId(bookId, userId);

        for (CartEntity items:cartEntity) {
            items.qty = qty;
            cartRepository.saveAndFlush(
                    items
            );
        }
    }
}