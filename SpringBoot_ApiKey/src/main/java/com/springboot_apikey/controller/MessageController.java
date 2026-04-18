package com.springboot_apikey.controller;

import com.springboot_apikey.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MessageController {


    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/public/hello")
    public String publicHello() {
        return messageService.getPublicMessage();
    }

    @GetMapping("/api/hello")
    public String apiHello() {
        return messageService.getProtectedMessage();
    }
}
