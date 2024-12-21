package com.example.Assignment.services;

import com.example.Assignment.models.product.Product;
import com.example.Assignment.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public long createProduct(Product product){
        Product product1 = productRepository.save(product);
        return product1.getId();
    }

    public Product findProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }
}
