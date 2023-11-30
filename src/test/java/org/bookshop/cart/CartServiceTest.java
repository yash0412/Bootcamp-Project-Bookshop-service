package org.bookshop.cart;

import org.assertj.core.api.Assertions;
import org.bookshop.book.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    CartRepository repository;
    @MockBean
    BookService bookService;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(CartRepository.class);
        bookService = Mockito.mock(BookService.class);
    }

    @Test
    void shouldGetAllCartItemsFromRepository() {
        Mockito.when(repository.findCartEntitiesById_UserId("123"))
                .thenReturn(List.of(new CartEntity(new UserBookKey("123", "123"),
                        1)));

        CartService cartService = new CartService(repository, bookService);

        List<Item> expectedBooks = List.of(new Item(
                "123",
                1));
        List<Item> allItemItems = cartService.getCartItems("123");

        Assertions.assertThat(allItemItems).containsAll(expectedBooks);
    }

    @Test
    void shouldCreateCartItem() {
        CartService cartService = new CartService(repository, bookService);
        UserBookKey userBookKey = new UserBookKey("1", "1");
        CartEntity cartEntity = new CartEntity(userBookKey, 1);
        cartService.createCartItem(
                userBookKey,
                1
        );
        Mockito.verify(repository, Mockito.times(1)).saveAndFlush(cartEntity);
    }

    @Test
    void shouldUpdateCartItem() {
        CartService cartService = new CartService(repository, bookService);
        UserBookKey userBookKey = new UserBookKey("1", "123");
        CartEntity cartEntity = new CartEntity(userBookKey,
                100);
        Mockito.when(repository.findCartEntitiesById(userBookKey)).
                thenReturn(List.of(cartEntity));
        cartService.updateCartItem(
                "123",
                "1",
                100
        );
        Mockito.verify(repository, Mockito.times(1)).saveAndFlush(cartEntity);
    }

    @Test
    void shouldDeleteCartItemSuccessfully() {
        CartRepository repository = Mockito.mock(CartRepository.class);
        CartService cartService = new CartService(repository, bookService);
        UserBookKey userBookKey = new UserBookKey("1", "123");
        cartService.deleteCartItem(
                "1",
                "123"
        );
        Mockito.verify(repository, Mockito.times(1)).deleteById(userBookKey);
    }

//    @Test
//    void shouldGetItemsFromCartAndGetBookDetailsUsingBookIdsAndReturnNoError() {
//        Book book = new Book("123",
//                "ISBN",
//                "Think And Grow Rich",
//                "Description",
//                "Nepolean Hill",
//                "1980",
//                120.00, 2, "imageUrl", "shortURL", 4.5);
//        Mockito.when(repository.findCartEntitiesById_UserId("userId")).thenReturn(List.of(
//                new CartEntity(new UserBookKey("userId", "123"), 3)
//        ));
//        Mockito.when(bookService.getBooks(List.of("123")))
//                .thenReturn(List.of(book));
//
//        CartService cartService = new CartService(repository, bookService);
//        CheckoutValidationResponse response = cartService.validateCheckout("userId");
//        Assertions.assertThat(response.error()).isEqualTo("");
//    }
}