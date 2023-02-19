package com.rviewer.skeletons.infrastructure.provider;

import com.rviewer.skeletons.domain.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken bearerTokenAuthenticationToken = (BearerTokenAuthenticationToken) authentication;
        String jwt = bearerTokenAuthenticationToken.getToken();

        log.info("Trying to decode token {}", jwt);

        String username = tokenService.decode(jwt);
        bearerTokenAuthenticationToken.setAuthenticated(username != null);
        bearerTokenAuthenticationToken.setDetails(username);

        log.info("Token {} successfully decoded", jwt);

        return bearerTokenAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BearerTokenAuthenticationToken.class);
    }
}
