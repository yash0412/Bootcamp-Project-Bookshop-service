package org.bookshop;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc
public class CartControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    void getEmptyCartItems() throws Exception {
        String uuid = "123";
        mockMvc.perform(get("/cart-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carts").isArray());
    }

    @Test
    void shouldReturnCartItems() throws Exception{
        String uuid = "123";
        Mockito.when(cartService.getCartItems(uuid)).thenReturn((List.of(new Cart("1", uuid, "book-1", 1))));
        mockMvc.perform(get("/cart-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carts[0].id").value("1"));
    }
}
