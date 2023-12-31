package org.bookshop.order;

import org.assertj.core.api.Assertions;
import org.bookshop.book.Book;
import org.bookshop.book.BookService;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @MockBean
    CartService cartService;
    @MockBean
    OrderItemRepository orderItemRepository;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    BookService bookService;

    private final String userId = "jhon11";
    private final String orderId = "OD123";

    @BeforeEach
    void setUp() {
        cartService = Mockito.mock(CartService.class);
        orderItemRepository = Mockito.mock(OrderItemRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        bookService = Mockito.mock(BookService.class);
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
        OrderService orderService = new OrderService(cartService, orderItemRepository, orderRepository, bookService);
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
        OrderService orderService = new OrderService(cartService, orderItemRepository, orderRepository, bookService);
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
        OrderService orderService = new OrderService(cartService, orderItemRepository, orderRepository, bookService);
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
        OrderService orderService = new OrderService(cartService, orderItemRepository, orderRepository, bookService);
        Assertions.assertThatThrownBy(() -> orderService.createOrder(userId, deliveryAddress, PaymentType.COD, "123"))
                .isInstanceOf(OrderCreationFailedException.class).hasMessage("Failed to create order");
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderIsNotPresentInDBForConfirm() {
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        OrderService orderService = new OrderService(cartService, orderItemRepository, orderRepository, bookService);
        Assertions.assertThatThrownBy(() -> orderService.confirmOrder(orderId, "123", OrderStatus.Completed))
                .isInstanceOf(OrderNotFoundException.class).hasMessage("Order id not found");
    }

    @Test
    void shouldConfirmOrderAndUpdateStatus() throws Exception {
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(new OrderEntity()));
        OrderService orderService = new OrderService(cartService, orderItemRepository, orderRepository, bookService);
        orderService.confirmOrder(orderId, "123", OrderStatus.Completed);
    }
}
