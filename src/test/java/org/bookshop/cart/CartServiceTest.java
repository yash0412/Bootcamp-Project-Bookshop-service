package org.bookshop.cart;

import org.assertj.core.api.Assertions;
import org.bookshop.book.Book;
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
        List<CartDetails> expectedBooks = List.of();
        List<CartDetails> allItemItems = cartService.getCartItems("123");

        Assertions.assertThat(allItemItems).containsAll(expectedBooks);
    }

    @Test
    void shouldCreateCartItemIfBookExistInBooksAndNotExistInCart() {
        CartService cartService = new CartService(repository, bookService);
        UserBookKey userBookKey = new UserBookKey("1", "1");
        Mockito.when(bookService.isBookExist("1")).
                thenReturn(true);
        Mockito.when(repository.existsById(userBookKey)).
                thenReturn(false);
        CartEntity cartEntity = new CartEntity(userBookKey, 1);
        cartService.createCartItem(
                userBookKey,
                1
        );
        Mockito.verify(repository, Mockito.times(1)).saveAndFlush(cartEntity);
    }

    @Test
    void shouldNotCreateCartItemIfBookDoesntExistInBooks() {
        CartService cartService = new CartService(repository, bookService);
        UserBookKey userBookKey = new UserBookKey("1", "1");
        Mockito.when(bookService.isBookExist("1")).
                thenReturn(false);
        Mockito.when(repository.existsById(userBookKey)).
                thenReturn(false);
        CartEntity cartEntity = new CartEntity(userBookKey, 1);
        cartService.createCartItem(
                userBookKey,
                1
        );
        Mockito.verify(repository, Mockito.times(0)).saveAndFlush(cartEntity);
    }

    @Test
    void shouldNotCreateCartItemIfBookDoesntExistInBooksAndAlreadyExistInCart() {
        CartService cartService = new CartService(repository, bookService);
        UserBookKey userBookKey = new UserBookKey("1", "1");
        Mockito.when(bookService.isBookExist("1")).
                thenReturn(false);
        Mockito.when(repository.existsById(userBookKey)).
                thenReturn(true);
        CartEntity cartEntity = new CartEntity(userBookKey, 1);
        cartService.createCartItem(
                userBookKey,
                1
        );
        Mockito.verify(repository, Mockito.times(0)).saveAndFlush(cartEntity);
    }

    @Test
    void shouldNotCreateCartItemIfBookExistInBooksAndNotExistInCart() {
        CartService cartService = new CartService(repository, bookService);
        UserBookKey userBookKey = new UserBookKey("1", "1");
        Mockito.when(bookService.isBookExist("1")).
                thenReturn(true);
        Mockito.when(repository.existsById(userBookKey)).
                thenReturn(true);
        CartEntity cartEntity = new CartEntity(userBookKey, 1);
        cartService.createCartItem(
                userBookKey,
                1
        );
        Mockito.verify(repository, Mockito.times(0)).saveAndFlush(cartEntity);
    }

    @Test
    void shouldUpdateCartItem() {
        CartService cartService = new CartService(repository, bookService);
        UserBookKey userBookKey = new UserBookKey("1", "123");
        CartEntity cartEntity = new CartEntity(userBookKey,
                100);
        Mockito.when(repository.findCartEntityById(userBookKey)).
                thenReturn(cartEntity);
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

    @Test
    void shouldGetItemsFromCartAndGetBookDetailsUsingBookIdsAndReturnNoError() {
        Book book = new Book("123",
                "ISBN",
                "Think And Grow Rich",
                "Description",
                "Nepolean Hill",
                "1980",
                120.00, 4, "imageUrl", "shortURL", 4.5);
        Mockito.when(repository.findCartEntitiesById_UserId("userId")).thenReturn(List.of(
                new CartEntity(new UserBookKey("userId", "123"), 3)
        ));
        Mockito.when(bookService.getBooks(List.of("123")))
                .thenReturn(List.of(book));

        CartService cartService = new CartService(repository, bookService);
        CheckoutValidationResponse response = cartService.validateCheckout("userId");
        Assertions.assertThat(response.error()).isEqualTo("");
    }

    @Test
    void shouldGetItemsFromCartAndGetBookDetailsUsingBookIdsAndReturnInvalidCartErrorWhenStockIsInSufficient() {
        Book book = new Book("123",
                "ISBN",
                "Think And Grow Rich",
                "Description",
                "Nepolean Hill",
                "1980",
                120.00, 2, "imageUrl", "shortURL", 4.5);
        Mockito.when(repository.findCartEntitiesById_UserId("userId")).thenReturn(List.of(
                new CartEntity(new UserBookKey("userId", "123"), 3)
        ));
        Mockito.when(bookService.getBooks(List.of("123")))
                .thenReturn(List.of(book));

        CartService cartService = new CartService(repository, bookService);
        CheckoutValidationResponse response = cartService.validateCheckout("userId");
        Assertions.assertThat(response.error()).isEqualTo("Invalid Cart");
        Assertions.assertThat(response.errorDetails().size()).isEqualTo(1);
        Assertions.assertThat(response.errorDetails().get(0)).isEqualTo(new BookWithQuantity("123", "Think And Grow Rich", 2));
    }
}