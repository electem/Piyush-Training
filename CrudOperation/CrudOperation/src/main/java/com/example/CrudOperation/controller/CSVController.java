package com.example.CrudOperation.controller;

import com.example.CrudOperation.service.CSVService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/csv")
public class CSVController {

    private final CSVService csvService;

    public CSVController(CSVService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam String url) {
        csvService.importFromUrl(url);
        return ResponseEntity.ok("CSV import started successfully");
    }
}