package com.authorize.authorization_server.service;

import com.authorize.authorization_server.domain.OAuthClient;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OauthClientService implements ClientDetailsService {
    OAuthClient oauthClient = new OAuthClient();

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        return new CustomOAuthClientDetails(oauthClient);
    }

    public Optional<OAuthClient> getOauthClientByClientId() {
        return Optional.of(oauthClient);
    }
}
