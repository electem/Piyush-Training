package com.onetomanymapping.service;

import com.onetomanymapping.entity.Orders;
import com.onetomanymapping.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrdersRepository ordRepo;
    
    public Orders saveOrder(Orders orders){
        return ordRepo.save(orders);
    }
    public List<Orders> getAllOrders() {
        return ordRepo.findAll();
    }

    public Orders getOrderById(Long id) {
        return ordRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orders not found with id: " + id));
    }

    public Orders updateOrder(Long id, Orders Orders) {
        Orders existingUser = ordRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orders not found with id: " + id));

        existingUser.setProd(Orders.getProd());

        return ordRepo.save(existingUser);
    }

    public void deleteOrder(Long id) {
        if (!ordRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Orders not found with id: " + id);
        }

        ordRepo.deleteById(id);
    }

}

