package com.example.security.service.impl;

import com.example.security.repository.RedisRepository;
import com.example.security.repository.model.User;
import com.example.security.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class RedisServiceImpl implements RedisService {

    private final RedisRepository redisRepository;

    @Override
    public Optional<User> getUserById(Long id){
        return redisRepository.getUserById(id);
    }

    @Override
    public void saveUser(User user){
        redisRepository.saveUser(user);
    }
}
