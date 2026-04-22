package com.example.CrudOperation.controller;

import com.example.CrudOperation.service.CSVService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/csv")
public class CSVController {

    private final CSVService csvService;

    public CSVController(CSVService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/import-url")
    public ResponseEntity<String> importFromUrl(@RequestParam("url") String url) {
        csvService.importFromUrl(url);

        System.out.println("===================");

        return ResponseEntity.ok("CSV imported from URL successfully");
    }
}
