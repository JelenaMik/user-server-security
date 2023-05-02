package com.example.security.service;

import com.example.security.repository.model.User;
import com.example.security.responsebodymodel.UserData;

import java.util.List;
import java.util.Optional;

public interface RedisService {
    Optional<User> getUserById(Long id);

    void saveUser(User user);

    void saveFavoriteProviderList(List<UserData> providerList, Long clientId);

    List<UserData> getProvidersListFromCache(Long clientId);

    void removeProviderList(Long clientId);

    Boolean doesProviderListExists(Long clientId);
}
