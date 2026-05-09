package com.order_management.controller;

import com.order_management.entity.Employee;
import com.order_management.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @PostMapping
    public ResponseEntity<Employee> saveEmp(@RequestBody Employee employee) {
        //logger.debug("debug the save api");
        logger.info("Received request to create employee");
        Employee savedEmp = employeeService.saveEmp(employee);
        return ResponseEntity.status(HttpStatus.OK).body(savedEmp);
    }
}
