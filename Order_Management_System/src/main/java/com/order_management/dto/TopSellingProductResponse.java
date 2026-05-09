package com.order_management.dto;

import lombok.Data;

@Data
public class TopSellingProductResponse {

    private Long productId;
    private String productName;
    private Double price;
    private Integer totalSoldQuantity;
}