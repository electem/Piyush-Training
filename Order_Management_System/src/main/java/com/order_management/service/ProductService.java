package com.order_management.service;

import com.order_management.dto.TopSellingProductProjection;
import com.order_management.dto.TopSellingProductResponse;
import com.order_management.entity.Product;
import com.order_management.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    public List<TopSellingProductResponse> getTopSellingProducts() {

        List<TopSellingProductProjection> results =
                productRepository.findTopSellingProducts();

        List<TopSellingProductResponse> responseList = new ArrayList<>();

        for (TopSellingProductProjection result : results) {

            TopSellingProductResponse response =
                    new TopSellingProductResponse();

            response.setProductId(result.getProductId());
            response.setProductName(result.getProductName());
            response.setPrice(result.getPrice());
            response.setTotalSoldQuantity(result.getTotalSoldQuantity());

            responseList.add(response);
        }

        return responseList;
    }
}
