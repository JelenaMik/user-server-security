package com.example.security.service;

import com.example.security.auth.AuthenticationResponse;

public interface UserService {
    public AuthenticationResponse changePassword(Long userId, String password);
    public AuthenticationResponse changeEmail(Long userId, String email);
}
