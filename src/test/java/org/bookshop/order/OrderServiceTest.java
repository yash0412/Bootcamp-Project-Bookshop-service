package org.bookshop.order;

import org.assertj.core.api.Assertions;
import org.bookshop.book.Book;
import org.bookshop.cart.CartDetails;
import org.bookshop.cart.CartService;
import org.bookshop.cart.CheckoutValidationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @MockBean
    CartService cartService;
    @MockBean
    OrderItemRepository orderItemRepository;
    @MockBean
    OrderRepository orderRepository;

    private final String userId = "jhon11";

    @BeforeEach
    void setUp() {
        cartService = Mockito.mock(CartService.class);
        orderItemRepository = Mockito.mock(OrderItemRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
    }

    @Test
    void shouldCreateTheOrderSuccessfully() throws Exception {
        DeliveryAddress deliveryAddress = new DeliveryAddress(
                "Kormangala, Bangalore", "Karnataka",
                "560110", Country.India, "9199002233", "8999299992");
        Book mockBook = new Book("abcd",
                "ISBN",
                "Think And Grow Rich",
                "Description",
                "Napolean Hill",
                "1980",
                120.00, 50, "imageUrl", "shortImage", 4.5);
        Mockito.when(cartService.validateCheckout(userId)).thenReturn(new CheckoutValidationResponse("", null));
        Mockito.when(cartService.getCartItems(userId)).thenReturn(List.of(new CartDetails(5, mockBook)));
        OrderService orderService = new OrderService(cartService, orderItemRepository, orderRepository);
        orderService.createOrder(userId, deliveryAddress, PaymentType.COD, "123");
    }

    @Test
    void shouldThrowExceptionWithMessageAsItemsOutOfStockWhenCartValidationFails() throws Exception {
        DeliveryAddress deliveryAddress = new DeliveryAddress(
                "Kormangala, Bangalore", "Karnataka",
                "560110", Country.India, "9199002233", "8999299992");
        Book mockBook = new Book("abcd",
                "ISBN",
                "Think And Grow Rich",
                "Description",
                "Napolean Hill",
                "1980",
                120.00, 50, "imageUrl", "shortImage", 4.5);
        Mockito.when(cartService.validateCheckout(userId)).thenReturn(new CheckoutValidationResponse("Invalid", null));
        OrderService orderService = new OrderService(cartService, orderItemRepository, orderRepository);
        Assertions.assertThatThrownBy(() -> orderService.createOrder(userId, deliveryAddress, PaymentType.COD, "123"))
                .isInstanceOf(InvalidCartException.class).hasMessage("Some order items are out of stock");
    }

    @Test
    void shouldThrowExceptionWithMessageAsEmptyCartWhenCartHasNoItems() throws Exception {
        DeliveryAddress deliveryAddress = new DeliveryAddress(
                "Kormangala, Bangalore", "Karnataka",
                "560110", Country.India, "9199002233", "8999299992");

        Mockito.when(cartService.validateCheckout(userId)).thenReturn(new CheckoutValidationResponse("", null));
        Mockito.when(cartService.getCartItems(userId)).thenReturn(List.of());
        OrderService orderService = new OrderService(cartService, orderItemRepository, orderRepository);
        Assertions.assertThatThrownBy(() -> orderService.createOrder(userId, deliveryAddress, PaymentType.COD, "123"))
                .isInstanceOf(InvalidCartException.class).hasMessage("No items in cart");
    }

    @Test
    void shouldThrowExceptionWithMessageAsFailedToCreateOrderWhenOrderCreationFails() throws Exception {
        DeliveryAddress deliveryAddress = new DeliveryAddress(
                "Kormangala, Bangalore", "Karnataka",
                "560110", Country.India, "9199002233", "8999299992");
        Book mockBook = new Book("abcd",
                "ISBN",
                "Think And Grow Rich",
                "Description",
                "Napolean Hill",
                "1980",
                120.00, 50, "imageUrl", "shortImage", 4.5);

        Mockito.when(cartService.validateCheckout(userId)).thenReturn(new CheckoutValidationResponse("", null));
        Mockito.when(cartService.getCartItems(userId)).thenReturn(List.of(new CartDetails(5, mockBook)));
        Mockito.when(orderRepository.saveAndFlush(Mockito.any())).thenThrow(new RuntimeException("failed"));
        OrderService orderService = new OrderService(cartService, orderItemRepository, orderRepository);
        Assertions.assertThatThrownBy(() -> orderService.createOrder(userId, deliveryAddress, PaymentType.COD, "123"))
                .isInstanceOf(OrderCreationFailedException.class).hasMessage("Failed to create order");
    }
}
