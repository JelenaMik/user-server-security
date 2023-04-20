package com.example.security.web;

import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.auth.AuthenticationService;
import com.example.security.responseBodyModel.UserData;
import com.example.security.service.UserDataService;
import com.example.security.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

//@Api(tags = "User login Controller", description = "Operations performing on registration and login")
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    public static final String USER_SERVICE = "http://localhost:8102/api/v1/userdata/";
    private final UserService userService;
    private final AuthenticationService service;

//    private final RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private final RestTemplate restTemplate;
    private final UserDataService userDataService;

    //    Testing
    @GetMapping("/hi")
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok("Hi from secured!");
    }

    @GetMapping("/all-user-data")
    public ResponseEntity<List<UserData>> allUsersData() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
        List<UserData> list = restTemplate.getForObject(USER_SERVICE+"all-users-data", List.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user-data/{id}")
    @CircuitBreaker(
            name = "user-data-service", fallbackMethod = "hardCodedUserData"
    )
    public ResponseEntity<UserData> userData(@PathVariable Long id) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
        UserData userData = restTemplate.getForObject(USER_SERVICE+"get-data/" + id, UserData.class);
        return ResponseEntity.ok(userData);
    }

    public ResponseEntity<UserData> hardCodedUserData(Long id, Exception e) {
        UserData userData =
                new UserData(null, id, "First Name", "Last Name", LocalDateTime.now());
        return ResponseEntity.ok(userData);
    }


    @PutMapping("/update-user-data")
    public ResponseEntity<HttpStatus> updateData(@RequestBody UserData userData) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(USER_SERVICE+"update-user-data", userData);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<AuthenticationResponse> changePassword(@PathVariable Long id, String password) {
        return ResponseEntity.ok(userService.changePassword(id, password));
    }

    @PutMapping("/change-email/{id}")
    public ResponseEntity<AuthenticationResponse> changeEmail(@PathVariable Long id, String email) {
        return ResponseEntity.ok(userService.changeEmail(id, email));
    }
//    this works
//    @PostMapping(path ="/authenticate",
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//            produces = {
//                    MediaType.APPLICATION_JSON_VALUE
//            })
//    public ResponseEntity<AuthenticationResponse> authenticate(
//            String email, String password
//    ) {
//        log.info("in auth method");
//        AuthenticationRequest request = new AuthenticationRequest(email, password);
//        log.info(request);
//        return ResponseEntity.ok(service.authenticate(request));
//    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/get-providers")
    public ResponseEntity<List> getProviderList() {
        return ResponseEntity.ok(userDataService.getProviderListIfSearchingStringIsEmpty());
    }


}
