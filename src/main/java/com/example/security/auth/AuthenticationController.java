package com.example.security.auth;

import com.example.security.responseBodyModel.UserData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private RestTemplate restTemplate;

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/registration")
    public ResponseEntity<String> showInfo(){
        return ResponseEntity.ok("You need to register");
    }

    @PostMapping("save_user_details/{id}")
    public ResponseEntity<UserData> saveUserData(@PathVariable Long id, String firstName, String lastName){

    UserData userData = new UserData();
    userData.setLastName(lastName);
    userData.setUserId(id);
    userData.setFirstName(firstName);

        UserData userDataSaved = restTemplate.postForObject("http://user-data-service/api/v1/userdata/save", userData,  UserData.class);

        return ResponseEntity.ok(userDataSaved);
}




//    @GetMapping("/all-user-data")
//    public ResponseEntity<List<UserData>> allUsersData(){
//
//        List<UserData> list = restTemplate.getForObject("http://user-data-service/api/v1/userdata/all-users-data",  List.class);
//
//        return ResponseEntity.ok(list);
//    }



}