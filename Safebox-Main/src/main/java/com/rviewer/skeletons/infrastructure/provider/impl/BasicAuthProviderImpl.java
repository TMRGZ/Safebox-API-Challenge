package com.rviewer.skeletons.infrastructure.provider.impl;

import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.exception.UserIsLockedException;
import com.rviewer.skeletons.domain.exception.UserIsUnauthorizedException;
import com.rviewer.skeletons.domain.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BasicAuthProviderImpl implements AuthenticationProvider {

    @Autowired
    private LoginService loginService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UsernamePasswordAuthenticationToken authToken;

        try {
            String token = loginService.loginUser(username, password);
            authToken = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(), authentication.getCredentials(), new ArrayList<>());
            authToken.setDetails(token);

        } catch (UserDoesNotExistException e) {
            throw new UsernameNotFoundException("The user does not exist");
        } catch (UserIsUnauthorizedException e) {
            throw new BadCredentialsException("The user is unauthorized");
        } catch (UserIsLockedException e) {
            throw new LockedException("The user is locked");
        } catch (ExternalServiceException e) {
            throw new AuthenticationServiceException("The authentication server has failed");
        }

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
