package org.bookshop.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;


    @Test
    void shouldReturnBadRequestIfDeliveryAddressIsNull() throws Exception {

        OrderConfirmationRequest orderConfirmationRequest = new OrderConfirmationRequest(null, PaymentType.COD);


        mockMvc.perform(post("/order-confirmation").header("userId", "jhon22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderConfirmationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfPaymentTypeIsNull() throws Exception {
        DeliveryAddress deliveryAddress = new DeliveryAddress(
                "Kormangala, Bangalore", "Karnataka",
                "560110", Country.India, "9199002233", "8999299992");
        OrderConfirmationRequest orderConfirmationRequest = new OrderConfirmationRequest(deliveryAddress, null);

        Mockito.when(orderService.createOrder("jhon22", deliveryAddress, PaymentType.COD, ""))
                .thenThrow(new InvalidCartException("Items out of stock"));
        mockMvc.perform(post("/order-confirmation").header("userId", "jhon22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderConfirmationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnSuccessOnSuccessfulOrder() throws Exception {
        DeliveryAddress deliveryAddress = new DeliveryAddress(
                "Kormangala, Bangalore", "Karnataka",
                "560110", Country.India, "9199002233", "8999299992");
        OrderConfirmationRequest orderConfirmationRequest = new OrderConfirmationRequest(deliveryAddress, PaymentType.COD);
        Mockito.when(orderService.createOrder("jhon22", deliveryAddress, PaymentType.COD, ""))
                .thenReturn("OD1234");
        
        mockMvc.perform(post("/order-confirmation").header("userId", "jhon22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderConfirmationRequest)))
                .andExpect(status().isCreated()).andExpect(header().stringValues("orderId", "OD1234"));
    }

    @Test
    void shouldReturnErrorOnFailureOfCheckoutValidation() throws Exception {
        DeliveryAddress deliveryAddress = new DeliveryAddress(
                "Kormangala, Bangalore", "Karnataka",
                "560110", Country.India, "9199002233", "8999299992");
        OrderConfirmationRequest orderConfirmationRequest = new OrderConfirmationRequest(deliveryAddress, PaymentType.COD);

        Mockito.when(orderService.createOrder("jhon22", deliveryAddress, PaymentType.COD, ""))
                .thenThrow(new InvalidCartException("Items out of stock"));
        mockMvc.perform(post("/order-confirmation").header("userId", "jhon22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderConfirmationRequest)))
                .andExpect(status().isUnprocessableEntity());
    }
}
