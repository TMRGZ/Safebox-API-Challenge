package com.rviewer.skeletons.infrastructure.manager.impl;

import com.rviewer.skeletons.domain.exception.BadPasswordException;
import com.rviewer.skeletons.domain.exception.UserIsLockedException;
import com.rviewer.skeletons.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class SafeboxAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            userService.loginUser(name, password);

        } catch (UserIsLockedException lockedException){
            throw new LockedException("The account is locked");
        } catch (BadPasswordException passwordException) {
            throw new BadCredentialsException("Username or password wrong");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
    }
}
