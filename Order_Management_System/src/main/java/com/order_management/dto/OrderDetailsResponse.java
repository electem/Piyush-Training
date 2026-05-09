package com.order_management.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDetailsResponse {

    private Long orderId;
    private String orderDate;
    private Double totalAmount;

    private Long userId;
    private String userName;
    private String userEmail;

    private List<OrderItemDetails> items;
}
