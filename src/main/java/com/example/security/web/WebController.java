package com.example.security.web;

import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.auth.AuthenticationService;
import com.example.security.auth.RegisterRequest;
import com.example.security.repository.model.User;
import com.example.security.responseBodyModel.UserData;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/set-localstorage/{email}")
    public String fillLocalStorageValues(@PathVariable (value = "email") String email, Model model){

        Long userId = userService.getUserIdByEmail(email);
        UserData userData = restTemplate.getForObject("http://user-data-service/api/v1/userdata/get-data/"+userId,  UserData.class);
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userData.getFirstName());

        return "set-localstorage";
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
    public String authenticate(
            String email, String password
    ) {
        log.info("in auth method");
        AuthenticationRequest request = new AuthenticationRequest(email, password);
        AuthenticationResponse response = restTemplate.postForObject("http://security-service/api/v1/user/authenticate", request,  AuthenticationResponse.class);
        if(response.getToken()!=null){
            return "redirect:set-localstorage/"+email;
        }
        else return "redirect:login?status=user_not_found";
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
        } else
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
    public String saveUserData(UserData userData, Long userId, String role){
        userData.setUserId(userId);
        userService.changeUserRole(userId, role);
        UserData userDataSaved = restTemplate.postForObject("http://user-data-service/api/v1/userdata/save", userData,  UserData.class);
        return "redirect:login";
    }

    @GetMapping("/change-login-info")
    public String changeCredentials(){
        return "change-login-info";
    }




}
