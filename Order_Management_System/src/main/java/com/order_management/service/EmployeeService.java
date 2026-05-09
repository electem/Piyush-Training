package com.order_management.service;

import com.order_management.entity.Employee;
import com.order_management.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public Employee saveEmp(Employee employee) {
        logger.info("Saving employee with email: {}", employee.getEmail());

        try {
            Employee savedEmployee = employeeRepository.save(employee);
            logger.info("Employee saved successfully with ID: {}",savedEmployee.getId());
            return savedEmployee;

        } catch (Exception e) {
            logger.error("Error saving employee", e);
            throw e;
        }
    }
}
