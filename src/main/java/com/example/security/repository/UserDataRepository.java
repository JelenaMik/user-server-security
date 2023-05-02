package com.example.security.repository;

import com.example.security.responsebodymodel.UserData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Log4j2
public class UserDataRepository {

    private static final String USER_DATA_SERVER_BASE_URL="http://localhost:8102/api/v1/userdata/";
    private static final String USER_DATA_FAVORITE_URL="http://localhost:8102/api/v1/favorite/";

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    public List<UserData> searchUserDataByName(String string){
          List<UserData > list = restTemplate.getForObject(USER_DATA_SERVER_BASE_URL+"search-users?firstName=" + string, List.class);
        return  mapper.convertValue(list, new TypeReference<List<UserData>>() {});
    }

    public List<UserData> getProvidersData(List<Long> firstProviders){
        return restTemplate.exchange(
                "http://localhost:8102/api/v1/userdata/provider-data",
                HttpMethod.GET,
                new HttpEntity<>(firstProviders),
                List.class )
                .getBody();
    }

    public Optional<UserData> getUserDataByUserId(Long userId){
        return Optional.ofNullable(restTemplate.getForObject(USER_DATA_SERVER_BASE_URL+"get-data/"+userId,  UserData.class));
    }

    public void saveUserData(UserData userData){
         restTemplate.postForObject(USER_DATA_SERVER_BASE_URL+"save", userData,  UserData.class);
    }

    public void updateUserData(UserData userData){
        restTemplate.put(USER_DATA_SERVER_BASE_URL+"update-user-data", userData);
    }

    public List<UserData> getAllUsersData(){
        return restTemplate.getForObject(USER_DATA_SERVER_BASE_URL+"all-users-data", List.class);
    }

    public void addFavoriteProvider(Long clientId, Long providerId){
        restTemplate.exchange(USER_DATA_FAVORITE_URL+"add?clientId=" +clientId+"&providerId="+providerId, HttpMethod.POST, null, ResponseEntity.class);
    }

    public List<UserData> getFavoriteProviders(Long clientId) {
        List<UserData> list = restTemplate.getForObject(USER_DATA_FAVORITE_URL+"providers-list/"+clientId, List.class);
        return mapper.convertValue(list, new TypeReference<List<UserData>>() {});
    }

    public void deleteFavoriteProvider(Long clientId, Long providerId) {
        restTemplate.delete(USER_DATA_FAVORITE_URL+"remove?clientId=" + clientId + "&providerId="+providerId);
    }
}
