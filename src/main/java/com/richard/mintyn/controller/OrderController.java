package com.richard.mintyn.controller;


import com.richard.mintyn.model.Order;
import com.richard.mintyn.model.Product;
import com.richard.mintyn.repo.OrderRepo;
import com.richard.mintyn.repo.ProductRepo;
import com.richard.mintyn.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
   @Autowired
    OrderRepo orderRepo;
   @Autowired
    ProductRepo productRepo;
   @Autowired
    OrderService orderService;



    @PostMapping("/")
    public ResponseEntity<String> add(@RequestBody Map<String,String> body){
        //Get the product
        UUID id =UUID.fromString(body.get("id"));
        Product product = productRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));;
        try{
            int qty = Integer.parseInt(body.get("qty"));
            if(product.getAvailable() - qty < 0){
                return new ResponseEntity<> ("Product not available", HttpStatus.NOT_FOUND);
            }

            Order order = new Order();
            order.setName(body.get("name"));
            order.setPrice(product.getPrice());
            order.setPhone(body.get("phone"));
            order.setProduct(product);
            order.setQty(qty);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(body.get("date"));
            order.setDate(date);

            //Set the new product available
            product.setAvailable(product.getAvailable() - qty);
            productRepo.save(product);
            orderRepo.save(order);
            //Add to Kafka
            orderService.publishOrder(order);

            return new ResponseEntity<> ("Order added successful", HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<> ("An error occurred", HttpStatus.NOT_FOUND);
        }


    }



}
