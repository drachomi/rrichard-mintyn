package com.richard.mintyn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.richard.mintyn.model.Product;
import com.richard.mintyn.repo.OrderRepo;
import com.richard.mintyn.repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepo productRepo;

    @MockBean
    private OrderRepo orderRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "Test Product");
        requestBody.put("price", "10.0");
        requestBody.put("description", "This is a test product");
        requestBody.put("available", "5");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(requestBody.get("name"));
        product.setPrice(Double.parseDouble(requestBody.get("price")));
        product.setDescription(requestBody.get("description"));
        product.setAvailable(Integer.parseInt(requestBody.get("available")));

        when(productRepo.save(product)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("Product added successful"));
    }

    @Test
    public void testEditProduct() throws Exception {
        UUID id = UUID.randomUUID();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("id", id.toString());
        requestBody.put("name", "Test Product");
        requestBody.put("price", "10.0");
        requestBody.put("description", "This is a test product");
        requestBody.put("available", "5");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        Product product = new Product();
        product.setId(id);
        product.setName(requestBody.get("name"));
        product.setPrice(Double.parseDouble(requestBody.get("price")));
        product.setDescription(requestBody.get("description"));
        product.setAvailable(Integer.parseInt(requestBody.get("available")));

        when(productRepo.findById(id)).thenReturn(java.util.Optional.of(product));
        when(productRepo.save(product)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.put("/product/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("Product edited successful"));
    }

    @Test
    public void testListAvailableProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/available"))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }
}
