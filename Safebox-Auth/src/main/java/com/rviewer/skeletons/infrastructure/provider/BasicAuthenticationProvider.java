package com.rviewer.skeletons.infrastructure.provider;

import com.rviewer.skeletons.domain.exception.BadPasswordException;
import com.rviewer.skeletons.domain.exception.SafeboxAuthException;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.exception.UserIsLockedException;
import com.rviewer.skeletons.domain.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Component
public class BasicAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private LoginService loginService;

    @Override
    @Transactional(noRollbackFor = AuthenticationException.class)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UsernamePasswordAuthenticationToken authToken;

        try {
            log.info("Trying to authenticate user {}", username);

            loginService.loginUser(username, password);
            authToken = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(), authentication.getCredentials(), new ArrayList<>());

            log.info("User {} successfully authenticated", username);

        } catch (UserDoesNotExistException e) {
            log.error("User {} does not exist", username);
            authToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
        } catch (BadPasswordException e) {
            log.error("The password for {} is wrong", username);
            authToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
        } catch (UserIsLockedException e) {
            log.error("User {} is locked", username);
            throw new LockedException("The account is locked");
        } catch (SafeboxAuthException e) {
            log.error("An unknown error happened while trying to authenticate", e);
            throw new InternalAuthenticationServiceException("Something wrong happened");
        }

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
