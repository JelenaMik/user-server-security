package com.example.security.repository;

import com.example.security.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RedisRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    public Optional<User> getUserById(Long id){
        return Optional.ofNullable(
                (User) redisTemplate.opsForHash().get("userById", id.toString()));
    }

    public void saveUser(User user){
        redisTemplate.opsForHash().put("userById", user.getId().toString(), user);
    }
}
