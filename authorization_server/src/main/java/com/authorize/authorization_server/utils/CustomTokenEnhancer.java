package com.authorize.authorization_server.utils;

import com.authorize.authorization_server.domain.Cities;
import com.authorize.authorization_server.domain.Users;
import com.authorize.authorization_server.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
     UsersService usersService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
//        usersService.
        Map<String, Object> additionalInformation = new HashMap<>();

//        Optional<Users> optionalUsers = usersService.find(oAuth2Authentication.getUserAuthentication().getName());
        Optional<Users> optionalUsers = usersService.findByUsername(oAuth2Authentication.getUserAuthentication().getName());
        if (optionalUsers.isPresent() && optionalUsers.get().getCities() != null && optionalUsers.get().getCities().size() > 0){
            ArrayList<String> cityList = new ArrayList<>();
            for (Cities cities : optionalUsers.get().getCities()) {
                cityList.add(cities.getName());
            }

            additionalInformation.put("cities", cityList);
        }
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInformation);

        return oAuth2AccessToken;
    }
}
