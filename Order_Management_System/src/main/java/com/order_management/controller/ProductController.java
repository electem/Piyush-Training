package com.order_management.controller;

import com.order_management.dto.TopSellingProductResponse;
import com.order_management.entity.Product;
import com.order_management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product productAdd=productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.OK).body(productAdd);
    }
    @GetMapping("/top-selling")
    public List<TopSellingProductResponse> getTopSellingProducts() {
        return productService.getTopSellingProducts();
    }
}
