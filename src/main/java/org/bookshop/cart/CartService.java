package org.bookshop.cart;

import org.bookshop.book.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final BookService bookService;

    public CartService(CartRepository cartRepository, BookService bookService) {
        this.cartRepository = cartRepository;
        this.bookService = bookService;
    }

    public List<Item> getCartItems(String userId) {
        UserBookKey userBookId = new UserBookKey(userId, null);
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

        for (CartEntity items : cartEntity) {
            items.qty = qty;
            cartRepository.saveAndFlush(
                    items
            );
        }
    }

    public void deleteCartItem(String userId, String bookId) {
        UserBookKey userBookId = new UserBookKey(userId, bookId);
        cartRepository.deleteById(userBookId);
    }
    
    public CheckoutValidationResponse validateCheckout(String userId) {
        List<CartEntity> cartItems = cartRepository.findCartEntitiesById_UserId(userId);
        return new CheckoutValidationResponse("", List.of());
    }
}