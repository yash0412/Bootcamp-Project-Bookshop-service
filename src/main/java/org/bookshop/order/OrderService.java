package org.bookshop.order;

import org.bookshop.cart.CartDetails;
import org.bookshop.cart.CartService;
import org.bookshop.cart.CheckoutValidationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final CartService cartService;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    OrderService(CartService cartService, OrderItemRepository orderItemRepository, OrderRepository orderRepository) {
        this.cartService = cartService;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    public String createOrder(String userId, DeliveryAddress deliveryAddress, PaymentType paymentType, String paymentId)
            throws InvalidCartException, OrderCreationFailedException {
        CheckoutValidationResponse checkoutValidationResponse = cartService.validateCheckout(userId);
        if (!checkoutValidationResponse.error().isEmpty()) {
            throw new InvalidCartException("Some order items are out of stock");
        } else {
            try {
                OrderEntity order = createOrderEntity(userId, deliveryAddress, paymentType, paymentId);
                List<OrderItemEntity> orderItems = populateOrderItemsFromCart(userId, order.id);
                if (orderItems.isEmpty()) {
                    throw new InvalidCartException("No items in cart");
                }
                order.setTotalAmount(calculateItemTotal(orderItems));
                saveOrder(order);
                saveOrderItems(orderItems);
                removeOrderedItemsFromCart(orderItems, userId);
                return order.id;
            } catch (InvalidCartException e) {
                System.out.println("Cart is empty, order can't be created");
                throw e;
            } catch (Exception e) {
                System.out.printf("Exception while creating order, err: %s\n", e.getMessage());
                throw new OrderCreationFailedException("Failed to create order");
            }
        }
    }

    public void confirmOrder(String orderId, String paymentId, OrderStatus status) throws OrderNotFoundException {
        Optional<OrderEntity> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            OrderEntity orderEntity = order.get();
            orderEntity.setUpdatedDate();
            orderEntity.setPaymentId(paymentId);
            orderEntity.setStatus(status);
            orderRepository.save(orderEntity);
        } else {
            throw new OrderNotFoundException("Order id not found");
        }
    }

    private void saveOrder(OrderEntity order) {
        orderRepository.saveAndFlush(order);
    }


    private void saveOrderItems(List<OrderItemEntity> orderItems) {
        orderItemRepository.saveAllAndFlush(orderItems);
    }

    private void removeOrderedItemsFromCart(List<OrderItemEntity> orderItems, String userId) {
        orderItems.forEach(orderItemEntity -> {
            cartService.deleteCartItem(userId, orderItemEntity.id.getBookId());
        });
    }

    private List<OrderItemEntity> populateOrderItemsFromCart(String userId, String orderId) {
        List<CartDetails> cartDetailsList = cartService.getCartItems(userId);
        return cartDetailsList.stream().map(item -> new OrderItemEntity(orderId, item.book().id(), item.qty(), item.book().price())).toList();
    }

    private OrderEntity createOrderEntity(String userId, DeliveryAddress deliveryAddress, PaymentType paymentType, String paymentId) {
        return new OrderEntity(
                userId,
                deliveryAddress,
                0,
                paymentType,
                paymentId
        );
    }

    private double calculateItemTotal(List<OrderItemEntity> orderItemEntities) {
        double total = 0;
        for (OrderItemEntity item : orderItemEntities) {
            total += item.quantity * item.unit_price;
        }
        return total;
    }
}
