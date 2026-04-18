package com.springboot_apikey.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public String getPublicMessage() {
        return "Public endpoint is accessible without an API key";
    }

    public String getProtectedMessage() {
        return "Protected endpoint is accessible with a valid API key";
    }


}
