package com.example.security.web.serviceControllers;

import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.enums.Role;
import com.example.security.exceptions.UserIsNotClientException;
import com.example.security.exceptions.UserIsNotProviderException;
import com.example.security.exceptions.UserNotFoundException;
import com.example.security.model.UserData;
import com.example.security.repository.model.User;
import com.example.security.service.UserDataService;
import com.example.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/userservice")
public class WebControllerServiceMethods {

    private final UserService userService;
    private final UserDataService userDataService;

@GetMapping("/is-provider/{userId}")
    public Boolean isUserDataProvider(Long userId){
    return userService.isUserDataProvider(userId);
}

@GetMapping("/{userId}")
    public ResponseEntity<User> findById(@PathVariable Long userId){
    return ResponseEntity.ok(userService.getUserById(userId));
}

    @GetMapping("/search-providers/{string}")
    List<UserData> getProviderListIfSearchingStringHasText(@PathVariable String string, String token){
        return  userDataService.getProviderListIfSearchingStringHasText(string, token);
    }

    @GetMapping("/search-providers")
    List<UserData> getProviderListIfSearchingStringIsEmpty(){
        return  userDataService.getProviderListIfSearchingStringIsEmpty();
    }

    @GetMapping("/find-id/{email}")
    public Long getUserIdByEmail(@PathVariable String email){
        return  userService.getUserIdByEmail(email);
    }
    @GetMapping("/find-user/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email){
        return  userService.getUserByEmail(email);
    }

    @GetMapping("/find-role/{userId}")
    public ResponseEntity<String> getUserRoleById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserRoleById(userId));
    }

    //No auth requered
    @GetMapping("/exists/{email}")
    public Boolean checkIfEmailExists(@PathVariable String email){
        return userService.checkIfEmailExists(email);
    }

    @GetMapping("/change-role/{id}/{role}")
    public ResponseEntity changeRole(@PathVariable Long id, @PathVariable String role){
        userService.changeUserRole(id, role);
        return ResponseEntity.ok().build();
    }

    @PostMapping("change/{oldEmail}")
    public ResponseEntity<AuthenticationResponse> changePasswordAndEmail(@PathVariable String oldEmail, @RequestBody AuthenticationRequest user){
        return new ResponseEntity<>(userService.changeEmailAndPassword(user, oldEmail), HttpStatus.OK);
    }

    @GetMapping("/users")
    public List<User> findUsersBySearching(@RequestParam(required = false) String email){
        return userService.findUsersBySearching(email);
    }

    @PutMapping("/admin-change-password/{userId}")
    public String adminChangePassword( @PathVariable Long userId ){
        return userService.adminChangePassword(userId);
    }

    @GetMapping("/if-provider/{userId}")
    public Boolean isUserProvider(@PathVariable Long userId){
        return userService.isUserProvider(userId);
    }

    @GetMapping("/is-client/{userId}")
    public Boolean isUserClient(@PathVariable Long userId){
        return userService.isUserClient(userId);
    }

}
