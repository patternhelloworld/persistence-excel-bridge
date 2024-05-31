package io.github.patternknife.pxbsample.domain.traditionaloauth.api.v1;


import io.github.patternknife.pxbsample.domain.traditionaloauth.dto.SpringSecurityTraditionalOauthDTO;
import io.github.patternknife.pxbsample.domain.traditionaloauth.service.TraditionalOauthService;
import io.github.patternknife.pxbsample.config.response.GlobalSuccessPayload;
import io.github.patternknife.pxbsample.config.response.error.message.SecurityExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TraditionalOauthApi {

    private final TraditionalOauthService traditionalOauthService;

    @PostMapping("/traditional-oauth/token")
    public GlobalSuccessPayload<SpringSecurityTraditionalOauthDTO.TokenResponse> createAccessToken(
            @ModelAttribute SpringSecurityTraditionalOauthDTO.TokenRequest tokenRequest,
            @RequestHeader("Authorization") String authorizationHeader) throws IOException {
        switch(tokenRequest.getGrant_type()) {
            case "password":
                return new GlobalSuccessPayload<>(traditionalOauthService.createAccessToken(tokenRequest, authorizationHeader));
            case "refresh_token":
                return new GlobalSuccessPayload<>(traditionalOauthService.refreshAccessToken(tokenRequest, authorizationHeader));
            default:
                throw new IllegalStateException(SecurityExceptionMessage.WRONG_GRANT_TYPE.getMessage());
        }
    }

}
