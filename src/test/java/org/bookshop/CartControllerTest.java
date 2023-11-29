package org.bookshop;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc
public class CartControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @Test
    void getEmptyCartItems() throws Exception {
        mockMvc.perform(get("/cart-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carts").isArray());
    }

    @Test
    void shouldReturnCartItems() throws Exception{
        String userId = "1";
        Mockito.when(cartService.getCartItems(userId)).thenReturn((List.of(new Cart("1", userId, "book-1", 1))));
        mockMvc.perform(get("/cart-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carts[0].id").value("1"));
    }

    @Test
    void shouldReturnEmptyBook() throws Exception {
        String userId = "1";
        Mockito.when(cartService.getCartItems(userId)).thenReturn(List.of());

        mockMvc.perform(get("/cart-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carts").isEmpty());
    }

    @Test
    void shouldCreateCartItems() throws Exception{

        CartItem item1 = new CartItem("1", "1", 1);
        CartItem item2 = new CartItem("2", "1", 5);

        List<CartItem> cartItems = new ArrayList<CartItem>();
        cartItems.add(item1);
        cartItems.add(item2);

        CartRequest request = new CartRequest(cartItems);


        mockMvc.perform(post("/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}