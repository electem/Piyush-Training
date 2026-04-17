package com.onetomanymapping.controller;

import com.onetomanymapping.entity.Customer;
import com.onetomanymapping.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @PostMapping
    public Customer createUser(@RequestBody Customer Customer) {
        return customerService.saveUser(Customer);
    }

    @GetMapping
    public List<Customer> getUsers() {
        return customerService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Customer getUser(@PathVariable Long id) {
        return customerService.getUserById(id);
    }

    @PutMapping("/{id}")
    public Customer updateUser(@PathVariable Long id, @RequestBody Customer Customer) {
        return customerService.updateUser(id, Customer);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        customerService.deleteUser(id);
        return "Customer deleted";
    }
}
