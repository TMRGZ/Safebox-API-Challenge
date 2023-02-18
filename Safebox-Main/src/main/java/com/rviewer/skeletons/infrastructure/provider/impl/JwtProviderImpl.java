package com.rviewer.skeletons.infrastructure.provider.impl;

import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.exception.UserIsUnauthorizedException;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtProviderImpl implements AuthenticationProvider {

    @Autowired
    private TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken bearerTokenAuthenticationToken = (BearerTokenAuthenticationToken) authentication;
        String jwt = bearerTokenAuthenticationToken.getToken();

        try {
            User user = tokenService.decodeToken(jwt);
            bearerTokenAuthenticationToken.setAuthenticated(true);
            bearerTokenAuthenticationToken.setDetails(user.getUsername());

        } catch (UserDoesNotExistException e) {
            throw new UsernameNotFoundException("The user does not exist");
        } catch (UserIsUnauthorizedException e) {
            throw new BadCredentialsException("The user is unauthorized");
        } catch (ExternalServiceException e) {
            throw new AuthenticationServiceException("The authentication server has failed");
        }

        return bearerTokenAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BearerTokenAuthenticationToken.class);
    }
}
