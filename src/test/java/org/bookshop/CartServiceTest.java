package org.bookshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    CartRepository cartRepository;

    @Test
    void shouldGetAllCartItemsFromRepository() {
        Mockito.when(cartRepository.findAll())
                .thenReturn(List.of(new CartEntity("1",
                        "123",
                        "123",
                        1)));

        CartService cartService = new CartService(cartRepository);

        List<Cart> expectedBooks = List.of(new Cart("1",
                "123",
                "123",
                1));
        List<Cart> allCartItems = cartService.getCartItems("123");

        Assertions.assertThat(allCartItems).containsAll(expectedBooks);
    }

    @Test
    void shouldCreateCartItem(){
        CartRepository repository =  Mockito.mock(CartRepository.class);
        CartService cartService = new CartService(repository);
        CartEntity cartEntity = new CartEntity("123", "1", "1", 1);
        cartService.createCartItem(
                "123",
                "1",
                "1",
                1
        );
        Mockito.verify(repository, Mockito.times(1)).saveAndFlush(cartEntity);
    }

    @Test
    void shouldUpdateCartItem(){
        CartRepository repository =  Mockito.mock(CartRepository.class);
        CartService cartService = new CartService(repository);
        CartEntity cartEntity = new CartEntity("1",
                "123",
                "1",
                100);
        Mockito.when(repository.findCartEntitiesByBookIdAndUserId("123", "1")).
                thenReturn(List.of(cartEntity));
        cartService.updateCartItem(
                "123",
                "1",
                100
        );
        Mockito.verify(repository, Mockito.times(1)).saveAndFlush(cartEntity);
    }
}