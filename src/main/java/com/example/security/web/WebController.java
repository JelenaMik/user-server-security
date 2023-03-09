package com.example.security.web;

import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.auth.AuthenticationService;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping
public class WebController {

    @Autowired
    private RestTemplate restTemplate;

    private final UserService userService;
    private final AuthenticationService service;

    @GetMapping("/my-profile")
    public String loginSuccess() {
        return "my-profile";
    }

    @GetMapping("/user-info")
    public String showUserInfo(Model model){
//            model.addAttribute("userId", userId);
//            UserDataDto activeUser = new UserDataDto();
//            String role;
//            try{
//                activeUser = userDataService.getUser(userId);
//                role = userService.getUserRole(userId);
//            }catch (Exception e){
//                activeUser.setFirstName("User");
//                role="user";
//            }
            model.addAttribute("firstName", "User");

        return "user-info";
    }

    @PostMapping(path ="/authentication",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<AuthenticationResponse> authenticate(
            String email, String password
    ) {
        log.info("in auth method");
        AuthenticationRequest request = new AuthenticationRequest(email, password);
        log.info(request);
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/authenticate")
    public String returnPage(){
        return "authenticate";
    }







}
