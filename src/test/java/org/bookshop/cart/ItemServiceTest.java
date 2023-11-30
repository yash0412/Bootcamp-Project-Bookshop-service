package org.bookshop.cart;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    CartRepository cartRepository;

    @Test
    void shouldGetAllCartItemsFromRepository() {
        Mockito.when(cartRepository.findCartEntitiesById_UserId("123"))
                .thenReturn(List.of(new CartEntity(new UserBookKey("123", "123"),
                        1)));

        CartService cartService = new CartService(cartRepository);

        List<Item> expectedBooks = List.of(new Item(
                "123",
                1));
        List<Item> allItemItems = cartService.getCartItems("123");

        Assertions.assertThat(allItemItems).containsAll(expectedBooks);
    }

    @Test
    void shouldCreateCartItem(){
        CartRepository repository =  Mockito.mock(CartRepository.class);
        CartService cartService = new CartService(repository);
        UserBookKey userBookKey = new UserBookKey("1", "1");
        CartEntity cartEntity = new CartEntity(userBookKey, 1);
        cartService.createCartItem(
                userBookKey,
                1
        );
        Mockito.verify(repository, Mockito.times(1)).saveAndFlush(cartEntity);
    }

    @Test
    void shouldUpdateCartItem(){
        CartRepository repository =  Mockito.mock(CartRepository.class);
        CartService cartService = new CartService(repository);
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
}