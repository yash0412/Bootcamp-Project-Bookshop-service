package org.bookshop.cart;

import org.bookshop.book.Book;
import org.bookshop.book.BookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Book> books = bookService.getBooks(cartItems.stream().map(item -> item.id.getBookId()).toList());
        Map<String, Book> bookQuantityMap = new HashMap<>();
        List<BookWithQuantity> errorDetails = new ArrayList<>();

        for (Book book : books) {
            bookQuantityMap.put(book.id(), book);
        }

        for (CartEntity cartItem : cartItems) {
            Book bookItem = bookQuantityMap.get(cartItem.id.getBookId());

            if (bookItem != null) {
                if (bookItem.quantity() < cartItem.qty) {
                    errorDetails.add(new BookWithQuantity(bookItem.id(),
                            bookItem.title(),
                            bookItem.quantity()));
                }
            }
        }
        String errorMessage = "";
        if (!errorDetails.isEmpty()) {
            errorMessage = "Invalid Cart";
        }
        return new CheckoutValidationResponse(errorMessage, errorDetails);
    }
}