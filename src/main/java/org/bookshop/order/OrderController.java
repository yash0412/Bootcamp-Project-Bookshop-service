package org.bookshop.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin
@RestController
public class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("order-confirmation")
    public ResponseEntity<String> createOrderAndConfirm(
            @RequestBody(required = true) OrderConfirmationRequest req,
            @RequestHeader(value = "userId", required = true) String userId
    ) throws InvalidCartException, OrderCreationFailedException, OrderNotFoundException {
        if (req.deliveryAddress() == null || req.paymentType() == null) {
            return ResponseEntity.badRequest()
                    .body("Invalid Request, required params are missing");
        }

        String orderId = orderService.createOrder(userId, req.deliveryAddress(), req.paymentType(), "");
        if (req.paymentType() == PaymentType.COD) {
            orderService.confirmOrder(orderId, "NA", OrderStatus.Completed);
        }

        URI location = URI.create("localhost:8080");
        return ResponseEntity.created(location)
                .header("orderId", orderId).build();
    }
}
