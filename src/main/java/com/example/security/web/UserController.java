package com.example.security.web;

import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.auth.AuthenticationService;
import com.example.security.repository.model.User;
import com.example.security.responseBodyModel.UserData;
import com.example.security.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

//@Api(tags = "User login Controller", description = "Operations performing on registration and login")
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@EnableCaching
public class UserController {

    public static final String USER_SERVICE = "userService";
    private final UserService userService;
    private final AuthenticationService service;
    private final RestTemplate restTemplate;

    //    Testing
    @GetMapping("/hi")
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok("Hi from secured!");
    }

    @GetMapping("/all-user-data")
    public ResponseEntity<List<UserData>> allUsersData() {
        List<UserData> list = restTemplate.getForObject("http://user-data-service/api/v1/userdata/all-users-data", List.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user-data/{id}")
    @CircuitBreaker(
            name = "user-data-service", fallbackMethod = "hardCodedUserData"
    )
    public ResponseEntity<UserData> userData(@PathVariable Long id) {
        UserData userData = restTemplate.getForObject("http://user-data-service/api/v1/userdata/get-data/" + id, UserData.class);
        return ResponseEntity.ok(userData);
    }

    public ResponseEntity<UserData> hardCodedUserData(Long id, Exception e) {
        UserData userData =
                new UserData(null, id, "First Name", "Last Name", LocalDateTime.now());
        return ResponseEntity.ok(userData);
    }


    @PutMapping("/update-user-data")
    public ResponseEntity<HttpStatus> updateData(@RequestBody UserData userData) {
        restTemplate.put("http://user-data-service/api/v1/userdata/update-user-data", userData);
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

    @GetMapping("/admin-page")
    @Cacheable(value="users")
    public ResponseEntity<List<User>> showAdminPage(@RequestParam(required = false) String string){
//        redisTemplate.opsForHash().delete("admin", 1);
        log.info("Search by email {}", string);
        if (Objects.equals(string, "")) string="";
        return ResponseEntity.ok(userService.findUsersBySearching(string));
    }

    @GetMapping("/find-user/{id}")
    @Cacheable(value ="userWithId", key = "#id")
    public ResponseEntity<User> showAdminPage(@PathVariable Long id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }


}
