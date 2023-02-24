package com.example.security.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "User login Controller", description = "Operations performing on registration and login")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {



    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }

    @GetMapping("/hi")
    public ResponseEntity<String> sayHi(){
        return ResponseEntity.ok("Hi from secured");
    }

}
