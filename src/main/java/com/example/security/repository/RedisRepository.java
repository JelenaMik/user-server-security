package com.example.security.repository;

import com.example.security.repository.model.User;
import com.example.security.responsebodymodel.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public  void saveFavoriteProviderList(List<UserData> providerList, Long clientId){
        redisTemplate.opsForHash().put("favoriteProviders", clientId.toString(), providerList);
    }

    public List<UserData> getProviderList(Long clientId) {
        return (List<UserData>) redisTemplate.opsForHash().get("favoriteProviders", clientId.toString());
    }

    public void removeProviderList(Long clientId){
        redisTemplate.opsForHash().delete("favoriteProviders", clientId.toString());
    }

    public Boolean doesProviderListExists(Long clientId){
        return redisTemplate.opsForHash().hasKey("favoriteProviders", clientId.toString());
    }

}
