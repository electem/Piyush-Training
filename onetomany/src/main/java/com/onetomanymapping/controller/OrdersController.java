package com.onetomanymapping.controller;

import com.onetomanymapping.entity.Orders;
import com.onetomanymapping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping
    public Orders createOrder(@RequestBody Orders Orders) {
        return orderService.saveOrder(Orders);
    }

    @GetMapping
    public List<Orders> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Orders getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}")
    public Orders updateOrder(@PathVariable Long id, @RequestBody Orders Orders) {
        return orderService.updateOrder(id, Orders);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "Orders deleted";
    }
}
