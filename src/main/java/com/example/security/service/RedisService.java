package com.example.security.service;

import com.example.security.repository.model.User;

import java.util.Optional;

public interface RedisService {
    Optional<User> getUserById(Long id);

    void saveUser(User user);
}
