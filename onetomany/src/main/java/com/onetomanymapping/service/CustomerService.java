package com.onetomanymapping.service;

import com.onetomanymapping.entity.Customer;
import com.onetomanymapping.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository custrepo;

    public Customer saveUser(Customer Customer) {
        return custrepo.save(Customer);
    }

    public List<Customer> getAllUsers() {
        return custrepo.findAll();
    }

    public Customer getUserById(Long id) {
        return custrepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id));
    }

    public Customer updateUser(Long id, Customer Customer) {
        Customer existingUser = custrepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id));

        existingUser.setName(Customer.getName());

        return custrepo.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!custrepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id);
        }

        custrepo.deleteById(id);
    }

}
