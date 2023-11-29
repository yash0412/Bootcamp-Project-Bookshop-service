package org.bookshop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@CrossOrigin
@RestController
public class CartController {

    private CartService cartService;

    public CartController(final CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("cart-items")
    public ResponseEntity<Carts> getCartItems(){
        String userId = "1";

        List<Cart> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(new Carts(cartItems));
    }

    @PostMapping("cart-items")
    public ResponseEntity <String> createCartItems(
            @RequestBody  CartRequest req
    ){
        String uniqueID = UUID.randomUUID().toString();
        CartEntity cartEntity = cartService.createCartItem(
                uniqueID,
                req.bookId(),
                req.userId(),
                req.qty()
        );

        URI location = URI.create("localhost:8080");
        return ResponseEntity.created(location).header("MyResponseHeader", "MyValue").body("Added to cart successfully");

    }
}