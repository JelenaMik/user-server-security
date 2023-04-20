package com.example.security.repository;

import com.example.security.repository.model.User;
import com.example.security.responseBodyModel.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@RequiredArgsConstructor
@Repository
@Log4j2
public class UserDataRepository {

    private static final String USER_DATA_SERVER_BASE_URL="http://localhost:8102/api/v1/userdata/";
//    private final RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    public List<UserData> searchUserDataByName(String string){
//        RestTemplate restTemplate = restTemplateBuilder.build();
          List<UserData > list = restTemplate.getForObject(USER_DATA_SERVER_BASE_URL+"search-users?firstName=" + string, List.class);
        return  mapper.convertValue(list, new TypeReference<List<UserData>>() {});
    }

    public List<UserData> getProvidersData(List<Long> firstProviders){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.exchange("http://localhost:8102/api/v1/userdata/provider-data", HttpMethod.GET, new HttpEntity<>(firstProviders), List.class ).getBody();
    }

    public UserData getUserDataByUserId(Long userId){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(USER_DATA_SERVER_BASE_URL+"get-data/"+userId,  UserData.class);
    }

    public UserData saveUserData(UserData userData){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.postForObject(USER_DATA_SERVER_BASE_URL+"save", userData,  UserData.class);
    }

    public void updateUserData(UserData userData){
//        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(USER_DATA_SERVER_BASE_URL+"update-user-data", userData);
    }

}
