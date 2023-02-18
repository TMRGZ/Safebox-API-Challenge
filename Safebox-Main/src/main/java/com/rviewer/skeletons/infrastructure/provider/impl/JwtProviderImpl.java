package com.rviewer.skeletons.infrastructure.provider.impl;

import com.rviewer.skeletons.domain.exception.BadTokenException;
import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProviderImpl implements AuthenticationProvider {

    @Autowired
    private TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken bearerTokenAuthenticationToken = (BearerTokenAuthenticationToken) authentication;
        String jwt = bearerTokenAuthenticationToken.getToken();

        try {
            log.info("Trying to authenticate token {}", jwt);

            User user = tokenService.decodeToken(jwt);
            bearerTokenAuthenticationToken.setAuthenticated(true);
            bearerTokenAuthenticationToken.setDetails(user.getUsername());

            log.info("Token {} successfully authenticated", jwt);

        } catch (BadTokenException e) {
            log.error("Token {} can't be authenticated", jwt);

        } catch (ExternalServiceException e) {
            log.error("An unknown error happened while trying to verify {}", jwt);
            throw new InternalAuthenticationServiceException("The authentication server has failed");
        }

        return bearerTokenAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BearerTokenAuthenticationToken.class);
    }
}
