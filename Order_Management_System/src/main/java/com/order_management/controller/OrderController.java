package com.order_management.controller;

import com.order_management.dto.OrderDetailsResponse;
import com.order_management.dto.OrderRequest;
import com.order_management.entity.Order;
import com.order_management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable Long orderId) {
        OrderDetailsResponse response = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/orders")
    public List<OrderDetailsResponse> getAllOrdersByUser(@PathVariable Long userId) {
        return orderService.getAllOrdersByUser(userId);
    }
}
