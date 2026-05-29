//package com.externalApi.controller;
//
//import com.externalApi.dto.Posts;
//import com.externalApi.dto.SecAppUser;
//import com.externalApi.service.ExternalApiService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/external")
//    public class ExternalApiController {
//
//        @Autowired
//        private ExternalApiService service;
//
//        @GetMapping("/posts")
//        public List<Posts> getPosts() {
//            return service.getPosts();
//        }
//        @PostMapping("/save")
//        public ResponseEntity<String> createUser(@RequestBody SecAppUser user) {
//            String response = service.postPosts(user);
//
//            return ResponseEntity.ok(response);
//    }
//
//}
