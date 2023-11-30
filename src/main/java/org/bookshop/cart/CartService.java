package org.bookshop.cart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Item> getCartItems(String userId) {
        UserBookKey userBookId = new UserBookKey(userId,null);
        return cartRepository.findCartEntitiesById_UserId(userId)
                .stream()
                .map(cartEntity -> new Item(cartEntity.id.getBookId(), cartEntity.qty))
                .toList();
    }

    public CartEntity createCartItem(UserBookKey id, Integer qty) {
        return cartRepository.saveAndFlush(
                new CartEntity(
                        id,
                        qty
                )
        );
    }

    public void updateCartItem(String bookId, String userId, Integer qty) {
        UserBookKey userBookId = new UserBookKey(userId, bookId);
        List<CartEntity> cartEntity = cartRepository.findCartEntitiesById(userBookId);

        for (CartEntity items:cartEntity) {
            items.qty = qty;
            cartRepository.saveAndFlush(
                    items
            );
        }
    }
}