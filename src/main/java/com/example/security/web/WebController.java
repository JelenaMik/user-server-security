package com.example.security.web;

import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.auth.AuthenticationService;
import com.example.security.auth.RegisterRequest;
import com.example.security.repository.model.User;
import com.example.security.responsebodymodel.UserData;
import com.example.security.service.UserDataService;
import com.example.security.service.UserService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping
public class WebController {

    private final UserService userService;
    private final AuthenticationService service;
    private final UserDataService userDataService;

    @GetMapping("/my-profile")
    public String loginSuccess(@RequestParam(required = false) String string, Model model){

        List<UserData> providerList;
        if (Objects.equals(string, "")) string="";

        if (StringUtils.hasText(string)){
            providerList = userDataService.getProviderListIfSearchingStringHasText(string);
            log.info("List is {}", providerList);
        }
        else{
            providerList = userDataService.getProviderListIfSearchingStringIsEmpty();
            log.info("List is {}", providerList);
        }
        model.addAttribute("providerList", providerList);
        return "my-profile";

    }


    @PostMapping("/search-providers")
    public String searchProviders(String firstName){
        return "redirect:my-profile?string="+firstName;
    }

    @PostMapping("/favorite-providers")
    public String showFavoriteProviders(Long clientId){
        return "redirect:my-favorite/"+clientId;
    }

    @GetMapping("/my-favorite/{clientId}")
    public String showMyFavoriteProviders(@PathVariable Long clientId, Model model){
        model.addAttribute("favoriteProviders", userDataService.getFavoriteProviders(clientId));
        return "my-favorite";
    }


    @GetMapping("/set-localstorage/{email}")
//    @CircuitBreaker(name = "user-data-service", fallbackMethod = "defaultData")
//    Retry doesn't for properly after provider search was implemented.
//    @Retry(name="user-data-service", fallbackMethod = "defaultData")
    public String fillLocalStorageValues(@PathVariable (value = "email") String email, Model model){

        Long userId = userService.getUserIdByEmail(email);
        String role = userService.getUserRoleById(userId);
        UserData userData = userDataService.getUserDataByUserId(userId);
        model.addAttribute("email", email);
        model.addAttribute("role", role);
        model.addAttribute("userData", userData);

        return "set-localstorage";
    }
//    public String defaultData(String email, Model model, Exception e){
//        Long userId = userService.getUserIdByEmail(email);
//        model.addAttribute("email", email);
//        model.addAttribute("userData", new UserData(null, userId, "first Name", "last Name", LocalDateTime.now()));
//        return "set-localstorage";
//    }



    @PostMapping("/login-process")
    @Retry(name="security-service")
    public String authenticate( AuthenticationRequest authenticationRequest ) {
        try{
            log.info("try");
            userService.getTokenAfterAuthentication(authenticationRequest);
            return "redirect:set-localstorage/"+authenticationRequest.getEmail();}
        catch (Exception e){
            return "redirect:login?status=user_not_found";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(@RequestParam(name="status", required = false) String status,Model model){
        model.addAttribute("user", new User());
        model.addAttribute("status", status);
        return "register";
    }

    @PostMapping("/register")
    public String completeRegistration(User user, Model model){
        if(Boolean.TRUE.equals(userService.checkIfEmailExists(user.getEmail()))){
            return "redirect:register?status=email-exists";
        }
        var request = new RegisterRequest(user.getEmail(), user.getPassword());
        var response = service.register(request);
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
        model.addAttribute("userData", new UserData());
        return "complete-registration";
    }

    @PostMapping("/save-user-data")
    @Retry(name="user-data-service")
    public String saveUserData(UserData userData, Long userId, String role){
        userData.setUserId(userId);
        userService.changeUserRole(userId, role);
        userDataService.saveUserData(userData);
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
        UserData userData = UserData.builder()
                .firstName(newFirstName)
                .lastName(newLastName)
                .userId(userId)
                .build();
        userDataService.updateUserData(userData);
        return "redirect:update-user-profile?id="+userId;
    }

    @GetMapping("/update-user-profile")
    @Retry(name="security-service")
    public String setNewLocalStorage(Model model, Long id){
        UserData userData = userDataService.getUserDataByUserId(id);
        model.addAttribute("userData", userData);
        return "update-user-profile";
    }







}
