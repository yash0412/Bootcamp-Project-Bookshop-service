package org.bookshop.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody CartRequest req
    ){

        for (CartItem item:req.cartItems()){
            String uniqueID = UUID.randomUUID().toString();
            CartEntity cartEntity = cartService.createCartItem(
                    uniqueID,
                    item.bookId(),
                    item.userId(),
                    item.qty()
            );
        }

        URI location = URI.create("localhost:8080");
        return ResponseEntity.created(location).header("MyResponseHeader", "MyValue").body("Added to cart successfully");

    }

    @PutMapping("cart-items")
    public ResponseEntity <String> updateCartItems(
            @RequestBody  CartItem req
    ){
        cartService.updateCartItem(
                req.bookId(),
                req.userId(),
                req.qty()
        );

        URI location = URI.create("localhost:8080");
        return ResponseEntity.created(location).header("MyResponseHeader", "MyValue").body("Updated cart successfully");

    }

//    @DeleteMapping("cart-item")
//    public ResponseEntity <String> deleteCartItem(
//            @RequestBody  CartItem req
//    ) {
//         cartService.deleteCartItem(
//                 req.bookId(),
//                 req.userId()
//         );
//
//    }
}