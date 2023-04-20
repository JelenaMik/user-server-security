package com.example.security.service.impl;

import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.config.JwtService;
import com.example.security.enums.Role;
import com.example.security.exceptions.UserDataNotFoundException;
import com.example.security.exceptions.UserNotFoundException;
import com.example.security.repository.UserRepository;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.security.repository.model.User;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
//    private final RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public AuthenticationResponse changePassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse changeEmail(Long userId, String email) {
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        user.setEmail(email);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse changeEmailAndPassword(User user, String oldEmail) {
        User userWithId = userRepository.findByEmail(oldEmail).orElseThrow(RuntimeException::new);
        userWithId.setEmail(user.getEmail());
        userWithId.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userWithId);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public Long getUserIdByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) return user.get().getId();
        else throw new UserNotFoundException();
    }

    @Override
    public void changeUserRole(Long id, String role) {
        User user = userRepository.findById(id).orElseThrow(UserDataNotFoundException::new);
        user.setRole(Role.valueOf(role.toUpperCase()));
        userRepository.save(user);
    }


    @Override
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> findUsersBySearching(String email){
        log.info("email entered {}", email );
        if (StringUtils.hasText(email)) return userRepository.findFirst10ByEmailContaining(email);
        return userRepository.findFirst10ByOrderByIdAsc();
    }

    @Override
    public List<Long> findProvidersBySearching(){
        log.info("in user serviceimpl searching for 10 providers");
        List<User> listOfProviders = userRepository.findFirst10ByRole(Role.PROVIDER);
        List<Long> listWithProvidersId = new ArrayList<>();
        listWithProvidersId = listOfProviders.stream()
                .map(provider -> provider.getId())
                .toList();
        log.info("Provider list {}", listOfProviders);
        log.info("list of Id {}", listWithProvidersId);
        return listWithProvidersId;

    }

    @Override
    public Boolean isUserDataProvider(Long userId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        log.info("is User Data Provider {}", user);
        return user.getRole().equals(Role.PROVIDER);
    }


    @Override
    public String adminChangePassword( Long userId ){
        String newPassword = autogeneratePassword();
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return newPassword;
    }

    private String autogeneratePassword(){
        return  RandomStringUtils.randomGraph(8,16);

    }

    @Override
    public String getUserRoleById(Long userId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String role = user.getRole().toString().toLowerCase();
        log.info("users role is {}", role);
        return role;
    }

    @Override
    public String getTokenAfterAuthentication(AuthenticationRequest request){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        AuthenticationResponse response = restTemplate.postForObject("http://localhost:8101/api/v1/user/authenticate", request,  AuthenticationResponse.class);
        return response.getToken();
    }
    @Override
    public Boolean checkIfEmailExists(String email){
        return userRepository.existsByEmail(email);
    }


}
