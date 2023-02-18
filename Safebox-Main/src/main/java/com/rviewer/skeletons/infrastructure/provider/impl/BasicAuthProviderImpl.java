package com.rviewer.skeletons.infrastructure.provider.impl;

import com.rviewer.skeletons.domain.exception.*;
import com.rviewer.skeletons.domain.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
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
            log.info("Trying to authenticate user {}", username);

            String token = loginService.loginUser(username, password);
            authToken = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(), authentication.getCredentials(), new ArrayList<>());
            authToken.setDetails(token);

            log.info("User {} successfully authenticated", username);

        } catch (UserDoesNotExistException e) {
            log.error("User {} does not exist in the authentication server", username);
            authToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
        } catch (UserIsUnauthorizedException e) {
            log.error("User {} is not authorized in the authentication server", username);
            authToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
        } catch (UserIsLockedException e) {
            log.error("User {} is locked", username);
            throw new LockedException("The user is locked");
        } catch (SafeboxMainException e) {
            log.error("An unknown error happened while trying to authenticate {}", username);
            throw new InternalAuthenticationServiceException("The authentication server has failed");
        }

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
