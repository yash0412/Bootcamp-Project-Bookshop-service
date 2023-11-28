package org.bookshop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        String uuid = "123";

        List<Cart> cartItems = cartService.getCartItems(uuid);
        return ResponseEntity.ok(new Carts(cartItems));
    }

    @PostMapping("cart-items")
    public ResponseEntity <String> createCartItems(
            @RequestParam(name = "id") String id,
            @RequestParam (name = "bookId") String bookId,
            @RequestParam (name = "userId") String userId,
            @RequestParam (name = "qty") Integer qty
    ){


        CartEntity cartEntity = cartService.createCartItem(
                id,
                bookId,
                userId,
                qty
        );

        URI location = URI.create("localhost:8080");
        return ResponseEntity.created(location).header("MyResponseHeader", "MyValue").body("Hello World");

    }
}