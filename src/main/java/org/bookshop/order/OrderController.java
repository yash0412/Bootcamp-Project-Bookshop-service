package org.bookshop.order;

import org.bookshop.cart.CartService;
import org.bookshop.cart.CheckoutValidationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin
@RestController
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }


    @PostMapping("order-confirmation")
    public ResponseEntity<?> createCartItems(
            @RequestBody(required = true) OrderConfirmationRequest req,
            @RequestHeader(value = "userId", required = true) String userId
    ) {
        CheckoutValidationResponse checkoutValidationResponse = cartService.validateCheckout(userId);
        if (checkoutValidationResponse.error().isEmpty()) {
            URI location = URI.create("localhost:8080");
            return ResponseEntity.created(location)
                    .header("orderId", "OD1234").build();
        } else {
            return ResponseEntity.unprocessableEntity().body(checkoutValidationResponse);
        }
    }
}
