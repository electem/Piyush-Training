//package com.externalApi.service;
//
//import com.externalApi.dto.LoginRequest;
//import com.externalApi.dto.LoginResponse;
//import com.externalApi.dto.Posts;
//import com.externalApi.dto.SecAppUser;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Service
//public class ExternalApiService {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//
//    public List<Posts> getPosts() {
//        String url = "https://jsonplaceholder.typicode.com/posts";
//        ResponseEntity<Posts[]> response =
//                restTemplate.getForEntity(url, Posts[].class);
//
//        Posts[] postsArray = response.getBody();
//
//        return Arrays.asList(postsArray);
//    }
//
//    @Value("${app.auth.url}")
//    private String authUrl;
//
//    @Value("${app.auth.username}")
//    private String username;
//
//    @Value("${app.auth.password}")
//    private String password;
//
//    public String postPosts(SecAppUser user) {
//
//        String url = "http://localhost:8081/student";
//        String token=getToken();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // Request Entity
//        HttpEntity<SecAppUser> entity = new HttpEntity<>(user, headers);
//
//        // POST Call
//        ResponseEntity<String> response = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                entity,
//                String.class
//        );
//
//        return response.getBody();
//    }
//
//    public String getToken() {
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername(username);
//        loginRequest.setPassword(password);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);
//
//        ResponseEntity<LoginResponse> response = restTemplate.exchange(
//                authUrl,
//                HttpMethod.POST,
//                entity,
//                LoginResponse.class
//        );
//
//        return response.getBody().getToken();
//    }
//}
