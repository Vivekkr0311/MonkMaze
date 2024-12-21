package com.example.Assignment.controllers;

import com.example.Assignment.models.product.Product;
import com.example.Assignment.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody Product product){
        try{
            long id = productService.createProduct(product);
            Product product1 = productService.findProductById(id);
            if(product1 != null){
                return new ResponseEntity<>(
                        product1,
                        HttpStatus.CREATED
                );
            }
        } catch (Exception e){
            return new ResponseEntity<>(
                    "Product could not be created",
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                HttpStatus.BAD_REQUEST
        );
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(){
       List<Product> allProducts =  productService.findAllProducts();
       return new ResponseEntity<>(
               allProducts,
               HttpStatus.OK
       );
    }
}