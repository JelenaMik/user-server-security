package com.example.security.repository;

import com.example.security.repository.model.User;
import com.example.security.responseBodyModel.UserData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@RequiredArgsConstructor
@Repository
public class UserDataRepository {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    public List<UserData> searchUserDataByName(String string){

          List<UserData > list = restTemplate.getForObject("http://user-data-service/api/v1/userdata/search-users?firstName=" + string, List.class);
        return  mapper.convertValue(list, new TypeReference<List<UserData>>() {});
    }

    public List<UserData> getProvidersData(List<User> firstProviders){
        return restTemplate.exchange("http://user-data-service/api/v1/userdata/provider-data", HttpMethod.GET, new HttpEntity<>(firstProviders), List.class ).getBody();
    }

    public UserData getUserDataByUserId(Long userId){
        return restTemplate.getForObject("http://user-data-service/api/v1/userdata/get-data/"+userId,  UserData.class);
    }

    public UserData saveUserData(UserData userData){
        return restTemplate.postForObject("http://user-data-service/api/v1/userdata/save", userData,  UserData.class);
    }

    public void updateUserData(UserData userData){
        restTemplate.put("http://user-data-service/api/v1/userdata/update-user-data", userData);
    }

}
