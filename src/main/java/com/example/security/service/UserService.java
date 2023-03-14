package com.example.security.service;

import com.example.security.auth.AuthenticationResponse;
import com.example.security.repository.model.User;

public interface UserService {
    public AuthenticationResponse changePassword(Long userId, String password);
    public AuthenticationResponse changeEmail(Long userId, String email);

    AuthenticationResponse changeEmailAndPassword(User user, String oldEmail);

    Long getUserIdByEmail(String email);

    void changeUserRole(Long id, String role);
}
