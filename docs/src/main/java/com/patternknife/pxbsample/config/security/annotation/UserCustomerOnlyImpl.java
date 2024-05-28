package com.patternknife.pxbsample.config.security.annotation;

import com.patternknife.pxbsample.config.response.error.exception.auth.CustomAuthGuardException;
import com.patternknife.pxbsample.config.security.principal.AccessTokenUserInfo;
import com.patternknife.pxbsample.config.security.principal.AdditionalAccessTokenUserInfo;
import com.patternknife.pxbsample.config.security.util.SecurityUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserCustomerOnlyImpl {

    @Around("@annotation(com.patternknife.pxbsample.config.security.annotation.UserCustomerOnly)")
    public Object check(ProceedingJoinPoint joinPoint) throws Throwable {

        AccessTokenUserInfo accessTokenUserInfo = SecurityUtil.getAccessTokenUser();

        if(accessTokenUserInfo != null && (accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getUserType() != AdditionalAccessTokenUserInfo.UserType.CUSTOMER)){
            throw new CustomAuthGuardException("ID \"" + accessTokenUserInfo.getUsername() + "\" : Not in Customer Group");
        }

        return joinPoint.proceed();
    }
}