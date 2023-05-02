package com.example.security.service.impl;

import com.example.security.repository.RedisRepository;
import com.example.security.repository.model.User;
import com.example.security.responsebodymodel.UserData;
import com.example.security.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

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

    @Override
    public void saveFavoriteProviderList(List< UserData> providerList, Long clientId){redisRepository.saveFavoriteProviderList(providerList, clientId);}

    @Override
    public List<UserData> getProvidersListFromCache(Long clientId){
        return redisRepository.getProviderList(clientId);
    }

    @Override
    public void removeProviderList(Long clientId) {
        if(Boolean.TRUE.equals(redisRepository.doesProviderListExists(clientId))){
            redisRepository.removeProviderList(clientId);
            log.info("Client id {} providerList has been removed from Cache", clientId);
        }
    }
    @Override
    public Boolean doesProviderListExists(Long clientId){
       return redisRepository.doesProviderListExists(clientId);
    }
}
