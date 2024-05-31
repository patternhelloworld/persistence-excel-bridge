package io.github.patternknife.pxbsample.util.auth;

import io.github.patternknife.pxbsample.domain.customer.entity.Customer;
import io.github.patternknife.pxbsample.config.security.principal.AccessTokenUserInfo;

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
