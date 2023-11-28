package org.bookshop;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
public class CartController {

    private CartService cartService;

    public CartController(final CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("cart-items")
    public ResponseEntity<Carts> getCartItems(){
        String uuid = "123";

        List<Cart> cartItems = cartService.getCartItems(uuid);
        return ResponseEntity.ok(new Carts(cartItems));
    }

}
