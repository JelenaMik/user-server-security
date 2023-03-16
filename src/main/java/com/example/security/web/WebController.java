package com.example.security.web;

import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.auth.AuthenticationService;
import com.example.security.auth.RegisterRequest;
import com.example.security.repository.model.User;
import com.example.security.responseBodyModel.UserData;
import com.example.security.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

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

    @GetMapping("/set-localstorage/{email}")
//    @CircuitBreaker(name = "user-data-service", fallbackMethod = "defaultData")
    @Retry(name="user-data-service", fallbackMethod = "defaultData")
    public String fillLocalStorageValues(@PathVariable (value = "email") String email, Model model){

        Long userId = userService.getUserIdByEmail(email);
        UserData userData = restTemplate.getForObject("http://user-data-service/api/v1/userdata/get-data/"+userId,  UserData.class);
        model.addAttribute("email", email);
        model.addAttribute("userData", userData);

        return "set-localstorage";
    }
    public String defaultData(String email, Model model, Exception e){
        Long userId = userService.getUserIdByEmail(email);
        model.addAttribute("email", email);
        model.addAttribute("userData", new UserData(null, userId, "first Name", "last Name", LocalDateTime.now()));
        return "set-localstorage";
    }



    @GetMapping("/user-info")
    public String showUserInfo(){
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

        return "user-info";
    }

//    @PostMapping(path ="/authentication",
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//            produces = {
//                    MediaType.APPLICATION_ATOM_XML_VALUE,
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

    @GetMapping("/authenticate")
    public String returnPage(){
        return "authenticate";
    }

    @PostMapping("/login-process")
    @Retry(name="security-service")
    public String authenticate(
            String email, String password
    ) {
        AuthenticationRequest request = new AuthenticationRequest(email, password);
        try{
            AuthenticationResponse response = restTemplate.postForObject("http://security-service/api/v1/user/authenticate", request,  AuthenticationResponse.class);
            return "redirect:set-localstorage/"+email;}
        catch (Exception e){return "redirect:login?status=user_not_found";}
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String completeRegistration(User user){
        var request = new RegisterRequest(user.getEmail(), user.getPassword());
        AuthenticationResponse response = service.register(request);
        if (response!=null) {
            Long userId = userService.
                    getUserIdByEmail(user.getEmail());
            log.info("user with id: {} created", userId);
            return "redirect:complete-registration?userId="+userId;
        }
        return "redirect:register?status=unsuccessful_registration";
    }

    @GetMapping("/complete-registration")
    public String registration(Model model,  @RequestParam(name="userId", required = false)Long userId){
        model.addAttribute("userId", userId);
        log.info("userid: {}", userId);
        UserData userData =  new UserData();
        model.addAttribute("userData", userData);
        return "complete-registration";
    }

    @PostMapping("/save-user-data")
    @Retry(name="user-data-service")
    public String saveUserData(UserData userData, Long userId, String role){
        userData.setUserId(userId);
        userService.changeUserRole(userId, role);
        UserData userDataSaved = restTemplate.postForObject("http://user-data-service/api/v1/userdata/save", userData,  UserData.class);
        return "redirect:login";
    }

    @GetMapping("/change-login-info")
    public String changeCredentials(Model model, String status){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("status", status);
        return "change-login-info";
    }

    @PostMapping("/update-login-info")
    public String updateLoginInfo(User user, String oldEmail){
        AuthenticationResponse token = userService.changeEmailAndPassword(user, oldEmail);
        log.info(token.getToken());
        //hande new token here
        return "redirect:change-login-info?status="+user.getEmail();

    }

    @GetMapping("/update-profile")
    public String updateUserData(String status,  Model model){
        model.addAttribute("status", status);
        return "update-profile";
    }

    @PostMapping("/update-user-data")
    @Retry(name="security-service")
    public String changeUserDat(Long userId, String newLastName, String newFirstName){
        UserData userData = new UserData();
        userData.setUserId(userId);
        userData.setFirstName(newFirstName);
        userData.setLastName(newLastName);
        restTemplate.put("http://security-service/api/v1/user/update-user-data", userData,  UserData.class);
        return "redirect:update-user-profile?id="+userId;
    }

    @GetMapping("/update-user-profile")
    @Retry(name="security-service")
    public String setNewLocalStorage(Model model, Long id){
        UserData userData = restTemplate.getForObject("http://security-service/api/v1/user/user-data/"+id,  UserData.class);
        model.addAttribute("userData", userData);
        return "update-user-profile";
    }



}
