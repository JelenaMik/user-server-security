package com.example.security.service;

import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.model.UserDto;
import com.example.security.repository.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public AuthenticationResponse changePassword(Long userId, String password);
    public AuthenticationResponse changeEmail(Long userId, String email);

    AuthenticationResponse changeEmailAndPassword(AuthenticationRequest user, String oldEmail);

    Long getUserIdByEmail(String email);

    Optional<UserDto> getUserByEmail(String email);

    void changeUserRole(Long id, String role);

    UserDto getUserById(Long id);

    List<UserDto> findUsersBySearching(String email);


    List<Long> findProvidersBySearching();

    Boolean isUserDataProvider(Long userId);

    Boolean isUserProvider(Long userId);

    Boolean isUserClient(Long clientId);

    String adminChangePassword(Long userId);

    String getUserRoleById(Long userId);

    String getTokenAfterAuthentication(AuthenticationRequest request);
    Boolean checkIfEmailExists(String email);
}
