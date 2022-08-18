package com.authorize.authorization_server.service;

import com.authorize.authorization_server.domain.OAuthClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;
import java.util.stream.Collectors;

public class CustomOAuthClientDetails implements ClientDetails {

    OAuthClient oAuthClient;

    public CustomOAuthClientDetails(OAuthClient OAuthClient) {
        this.oAuthClient = new OAuthClient();
    }

    @Override
    public String getClientId() {
        return oAuthClient.getClientId();
    }

    @Override
    public Set<String> getResourceIds() {
        return Arrays.stream(oAuthClient.getResourceIds().split(",")).collect(Collectors.toSet());
    }

    @Override
    public boolean isSecretRequired() {
        return oAuthClient.getIsSecretRequired();
    }

    @Override
    public String getClientSecret() {
        return oAuthClient.getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return oAuthClient.getIsScoped();
    }

    @Override
    public Set<String> getScope() {
        return Arrays.stream(oAuthClient.getScope().split(",")).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return Arrays.stream(oAuthClient.getAuthorizedGrantTypes().split(",")).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return Arrays.stream(oAuthClient.getRegisteredRedirectUri().split(",")).collect(Collectors.toSet());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Arrays.stream(oAuthClient.getAuthorities().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return oAuthClient.getAccessTokenValiditySeconds();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return oAuthClient.getRefreshTokenValiditySeconds();
    }

    @Override
    public boolean isAutoApprove(String s) {
        return oAuthClient.getIsAutoApprove();
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        try {
            return new ObjectMapper().readValue(oAuthClient.getAdditionalInformation(), new TypeReference<HashMap<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
