package org.bookshop.cart;

import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@CrossOrigin
@RestController
public class CartController {

    private final CartService cartService;

    public CartController(final CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("cart")
    public ResponseEntity<Cart> getCartItems(@RequestHeader(value = "userId", required = true) String userId) {

        List<CartDetails> cartItems = cartService.getCartItems(userId);

        return ResponseEntity.ok(new Cart(cartItems));
    }

    @PostMapping("cart")
    public ResponseEntity<String> createCartItems(
            @RequestBody CartRequest req,
            @RequestHeader(value = "userId", required = true) String userId
    ) {

        for (CartItem item : req.cartItems()) {
            cartService.createCartItem(
                    new UserBookKey(userId, item.bookId()),
                    item.qty()
            );
        }

        URI location = URI.create("localhost:8080");
        return ResponseEntity.created(location).build();

    }

    @PatchMapping("cart-item/{book-id}")
    public ResponseEntity<String> updateCartItems(
            @PathVariable("book-id") String bookId,
            @RequestBody Item req,
            @RequestHeader(value = "userId", required = true) String userId
    ) {
        cartService.updateCartItem(
                bookId,
                userId,
                req.qty()
        );

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("cart-item/{book-id}")
    public ResponseEntity <String> deleteCartItem(
            @PathVariable("book-id") String bookId,
            @RequestHeader(value = "userId", required = true) String userId
    ) {
         cartService.deleteCartItem(
                 userId, bookId
         );
        return ResponseEntity.ok("Deleted Successfully");
    }

    @GetMapping("checkoutValidation")
    public ResponseEntity<?> checkoutValidation(
            @RequestHeader(value = "userId", required = true) String userId
    ) {
        CheckoutValidationResponse checkoutValidationResponse = cartService.validateCheckout(userId);
        if (checkoutValidationResponse.error().equals("")) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.unprocessableEntity().body(checkoutValidationResponse);
        }
    }
}