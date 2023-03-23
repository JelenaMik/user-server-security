package com.example.security.web;

import com.example.security.auth.AuthenticationResponse;
import com.example.security.repository.model.User;
import com.example.security.responseBodyModel.UserData;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminWebController {
    private final UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/admin-page")
    public String showAdminPage(@RequestParam(required = false) String string, Model model){
        log.info("emailEntered from controller {}", string);
        if (Objects.equals(string, "")) string="";
        model.addAttribute("users", userService.findUsersBySearching(string));
        return "admin-page";
    }
    @PostMapping("/search-users")
    public String searchUsers(String emailEntered){
        return "redirect:admin-page?string="+emailEntered;
    }


    @PostMapping("/admin-change-user")
    public String shoeUserInfo(Long id){
        return "redirect:user-info?userId="+id;
    }

    @GetMapping("/user-info")
    public String userInfo(@RequestParam Long userId, Model model){
        User user = userService.getUserById(userId);
        UserData userData = restTemplate.getForObject("http://user-data-service/api/v1/userdata/get-data/"+userId,  UserData.class);
        model.addAttribute("user", user);
        model.addAttribute("userData", userData);
        return "user-info";

    }

    @PostMapping("update-password")
    public String updatePassword(Long userId){
        String newPassword = userService.adminChangePassword(userId);
        log.warn("Password was changed to {}", newPassword);
        // implement sending new password in email or message
        return "redirect:user-info?userId="+userId;
    }
}
