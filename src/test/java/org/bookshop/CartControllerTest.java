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

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

        CartRequest request = new CartRequest("1", "1", 1);
        mockMvc.perform(post("/cart-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}