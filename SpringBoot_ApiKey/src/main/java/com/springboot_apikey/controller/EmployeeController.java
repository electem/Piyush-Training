package com.springboot_apikey.controller;

import com.springboot_apikey.entity.Employee;
import com.springboot_apikey.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

        private final EmployeeService service;

        // CREATE
        @PostMapping
        public Employee createEmployee(@RequestBody Employee employee) {
            return service.saveEmployee(employee);
        }

        // READ ALL
        @GetMapping
        public List<Employee> getAllEmployees() {
            return service.getAllEmployees();
        }

        // READ BY ID
        @GetMapping("/{id}")
        public Employee getEmployee(@PathVariable Long id) {
            return service.getEmployeeById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
        }

        // UPDATE
        @PutMapping("/{id}")
        public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
            return service.updateEmployee(id, employee);
        }

        // DELETE
        @DeleteMapping("/{id}")
        public String deleteEmployee(@PathVariable Long id) {
            service.deleteEmployee(id);
            return "Employee deleted successfully";
        }

}
