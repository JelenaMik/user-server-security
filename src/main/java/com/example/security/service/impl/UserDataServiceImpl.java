package com.example.security.service.impl;

import com.example.security.repository.UserDataRepository;
import com.example.security.repository.model.User;
import com.example.security.responseBodyModel.UserData;
import com.example.security.service.UserDataService;
import com.example.security.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {

    private final UserDataRepository userDataRepository;
    private final UserService userService;

    @Override
    public List<UserData> getProviderListIfSearchingStringHasText(String string){
        List<UserData> list = userDataRepository.searchUserDataByName(string);
        log.debug("list is {}", list);
        List<UserData> providerList = list.stream()
                .filter(userData -> userService.isUserDataProvider(userData.getUserId()))
                .toList();
        log.debug("providerList mapped {}", providerList);
        return providerList;
    }
    @Override
    public List<UserData> getProviderListIfSearchingStringIsEmpty(){
        List<User> first10Providers = userService.findProvidersBySearching();
        log.debug("First ten users {}", first10Providers);
        List<UserData> providerData = userDataRepository.getProvidersData(first10Providers);
        log.debug("ProviderData list {}", providerData);
        return providerData;
    }

    @Override
    public UserData getUserDataByUserId(Long userId){
        return userDataRepository.getUserDataByUserId(userId);
    }

    @Override
    public UserData saveUserData(UserData userData){
        return userDataRepository.saveUserData(userData);
    }

    @Override
    public void updateUserData(UserData userData){
        userDataRepository.updateUserData(userData);
    }

}
