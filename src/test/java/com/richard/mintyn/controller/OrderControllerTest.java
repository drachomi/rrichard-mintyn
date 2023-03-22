package com.richard.mintyn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.richard.mintyn.model.Order;
import com.richard.mintyn.model.Product;
import com.richard.mintyn.repo.OrderRepo;
import com.richard.mintyn.repo.ProductRepo;
import com.richard.mintyn.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepo orderRepo;

    @MockBean
    private ProductRepo productRepo;

    @MockBean
    private OrderService orderService;

    private Product testProduct;

    @BeforeEach
    public void setup() {
        // set up a test product
        testProduct = new Product();
        testProduct.setId(UUID.randomUUID());
        testProduct.setName("Test Product");
        testProduct.setPrice(9.99);
        testProduct.setDescription("A product for testing");
        testProduct.setAvailable(10);
    }

    @Test
    void addOrderTest() throws Exception {
        // set up a test order request
        Map<String, String> orderRequest = new HashMap<>();
        orderRequest.put("id", testProduct.getId().toString());
        orderRequest.put("name", "Idio Akan");
        orderRequest.put("phone", "09034310695");
        orderRequest.put("qty", "5");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2023-03-21");
        orderRequest.put("date", formatter.format(date));

        // set up a test order response
        Order testOrder = new Order();
        testOrder.setId(UUID.randomUUID());
        testOrder.setName("Imoh Richard");
        testOrder.setPrice(testProduct.getPrice());
        testOrder.setPhone("09034310696");
        testOrder.setProduct(testProduct);
        testOrder.setQty(5);
        testOrder.setDate(date);

        when(productRepo.findById(any(UUID.class))).thenReturn(Optional.of(testProduct));
        when(orderRepo.save(any(Order.class))).thenReturn(testOrder);

        mockMvc.perform(post("/order/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Order added successful"));
    }

}
