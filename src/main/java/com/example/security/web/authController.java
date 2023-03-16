package com.example.security.web;

import com.example.security.auth.AuthenticationService;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.security.repository.model.User;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.auth.AuthenticationRequest;
import org.springframework.web.client.RestTemplate;

@Controller
@Log4j2
@RequiredArgsConstructor
public class authController {
    @Autowired
    private RestTemplate restTemplate;
    private final UserService userService;

    private final AuthenticationService authenticationService;


    @GetMapping("/login")
    public String login(@RequestParam(name="status", required = false) String status, Model model){
        model.addAttribute("status", status);
        return "login";
    }





}
