package com.example.security.service.impl;

import com.example.security.auth.AuthenticationResponse;
import com.example.security.config.JwtService;
import com.example.security.enums.Role;
import com.example.security.repository.UserRepository;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.security.repository.model.User;
@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse changePassword(Long userId, String password){
        User user = userRepository.findById(userId).orElseThrow( RuntimeException::new );
                user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse changeEmail(Long userId, String email){
        User user = userRepository.findById(userId).orElseThrow( RuntimeException::new );
        user.setEmail(email);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
