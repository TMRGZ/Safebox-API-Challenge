package com.rviewer.skeletons.infrastructure.provider.impl;

import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.exception.UserIsUnauthorizedException;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JwtProviderImpl implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken bearerTokenAuthenticationToken = (BearerTokenAuthenticationToken) authentication;
        String jwt = bearerTokenAuthenticationToken.getToken();
        UsernamePasswordAuthenticationToken authToken;

        try {
            User user = userService.getUser(jwt);
            authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, new ArrayList<>());

        } catch (UserDoesNotExistException e) {
            throw new UsernameNotFoundException("The user does not exist");
        } catch (UserIsUnauthorizedException e) {
            throw new BadCredentialsException("The user is unauthorized");
        } catch (ExternalServiceException e) {
            throw new AuthenticationServiceException("The authentication server has failed");
        }

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BearerTokenAuthenticationToken.class);
    }
}
