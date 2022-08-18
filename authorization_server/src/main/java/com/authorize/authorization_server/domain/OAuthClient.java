package com.authorize.authorization_server.domain;

import lombok.Data;

@Data
public class OAuthClient {

    private String clientId = "web";

    private String resourceIds = "api";

    private Boolean isSecretRequired = true;

    private String clientSecret = "$2a$10$kAJ0RxJm9G5iJv2CHE1p4OLVtnib2T31FBDYJbARohJctl/0HHJjm";

    private Boolean isScoped = true;

    private String scope = "READ,WRITE";

    private String authorizedGrantTypes = "authorization_code,password,refresh_token,implicit";

    private String registeredRedirectUri = "";

    private String authorities = "PROFILE";

    private Integer accessTokenValiditySeconds = 36000;

    private Integer refreshTokenValiditySeconds = 10000;

    private Boolean isAutoApprove = true;

    private String additionalInformation = "";

}
