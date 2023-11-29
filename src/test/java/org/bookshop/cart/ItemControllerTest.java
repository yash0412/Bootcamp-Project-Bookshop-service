package org.bookshop.cart;


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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @Test
    void getEmptyCartItems() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    void shouldReturnCartItems() throws Exception{
        String userId = "1";
        Mockito.when(cartService.getCartItems(userId)).thenReturn((List.of(new Item("1", userId, "book-1", 1))));
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value("1"));
    }

    @Test
    void shouldReturnEmptyBook() throws Exception {
        String userId = "1";
        Mockito.when(cartService.getCartItems(userId)).thenReturn(List.of());

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    void shouldCreateCartItems() throws Exception{

        CartItem item1 = new CartItem("1", "1", 1);
        CartItem item2 = new CartItem("2", "1", 5);

        List<CartItem> cartItems = new ArrayList<CartItem>();
        cartItems.add(item1);
        cartItems.add(item2);

        CartRequest request = new CartRequest(cartItems);


        mockMvc.perform(post("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateCartItems() throws Exception{
        CartItem request = new CartItem("1", "1", 1);
        mockMvc.perform(put("/cart-item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

//    @Test
//    void shouldDeleteCartItems() throws Exception{
//        CartItem request = new CartItem("1", "1", 1);
//        mockMvc.perform(delete("/cart-items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//    }
}