package com.externalApi.controller;

import com.externalApi.dto.PropertyType;
import com.externalApi.entity.Room;
import com.externalApi.service.NewApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/new")
public class NewController {

    @Autowired
    private NewApiService service;

    @GetMapping("/fetch")
    public List<PropertyType> fetchProp() {
        return service.fetchProp();
    }
    @GetMapping("/fetchandsave")
    public List<Room> fetchAndSave() {
        return service.fetchAndSave();
    }
}