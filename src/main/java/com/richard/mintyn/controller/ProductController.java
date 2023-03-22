package com.richard.mintyn.controller;


import com.richard.mintyn.model.Product;
import com.richard.mintyn.repo.OrderRepo;
import com.richard.mintyn.repo.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {
   @Autowired
    OrderRepo orderRepo;
   @Autowired
    ProductRepo productRepo;


    @PostMapping("/")
    public ResponseEntity<String> add(@RequestBody Map<String,String> body){
        //Get the body
        Product product = new Product();
        product.setName(body.get("name"));
        product.setPrice(Double.parseDouble(body.get("price")));
        product.setDescription(body.get("description"));
        product.setAvailable(Integer.parseInt(body.get("available")));


        try{
            productRepo.save(product);
            return new ResponseEntity<> ("Product added successful", HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<> ("An error occurred", HttpStatus.NOT_FOUND);
        }


    }

    @PutMapping("/")
    public ResponseEntity<String> edit(@RequestBody Map<String,String> body){
        try{
            UUID id =UUID.fromString(body.get("id"));
            Product product = productRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));;

            if (body.containsKey("name")) {
                product.setName(body.get("name"));
            }

            if (body.containsKey("price")) {
                product.setPrice(Double.parseDouble(body.get("price")));
            }

            if (body.containsKey("description")) {
                product.setDescription(body.get("description"));
            }

            if (body.containsKey("available")) {
                product.setAvailable(Integer.parseInt(body.get("available")));
            }


            productRepo.save(product);
        }catch (Exception e){
            return new ResponseEntity<> ("An error occurred", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> ("Product edited successful", HttpStatus.ACCEPTED);
    }

    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> listProducts(){
        Map<String, Object> res = new HashMap<>();

        try{
            List<Product>products = productRepo.findAll();

            if(products.isEmpty()){
                res.put("success", false);
                res.put("message","product are empty");
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            res.put("success", true);
            res.put("products",products.stream().filter(p -> p.getAvailable() > 0).collect(Collectors.toList()));


        }catch (Exception e){
            res.put("success", false);
            res.put("message","An error occured");
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> (res, HttpStatus.ACCEPTED);
    }



}
