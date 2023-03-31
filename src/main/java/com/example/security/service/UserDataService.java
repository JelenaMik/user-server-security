package com.example.security.service;

import com.example.security.responseBodyModel.UserData;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserDataService {
    List<UserData> getProviderListIfSearchingStringHasText(String string);

    List<UserData> getProviderListIfSearchingStringIsEmpty();

    UserData getUserDataByUserId(Long userId);

    UserData saveUserData(UserData userData);

    void updateUserData(UserData userData);
}
