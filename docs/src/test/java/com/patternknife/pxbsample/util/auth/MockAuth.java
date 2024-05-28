package com.patternknife.pxbsample.util.auth;

import com.patternknife.pxbsample.domain.customer.entity.Customer;
import com.patternknife.pxbsample.config.security.principal.AccessTokenUserInfo;

public interface MockAuth {

    /**
     * Mock @AuthenticationPrincipal
     */
    AccessTokenUserInfo mockAuthenticationPrincipal(Customer customer);

    /**
     * Mock Customer
     */
    Customer mockCustomerObject() throws Exception;

    /**
     * Mock AccessToken
     */
    String mockAccessToken(String clientName, String clientPassword, String username, String password) throws Exception;

    /**
     * Mock AccessToken on entity (select from DB)
     */
    String mockAccessTokenOnPersistence(String authUrl, String clientName, String clientPassword, String username, String password) throws Exception;
}
