package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.*;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.LoginService;
import com.rviewer.skeletons.infrastructure.mapper.UserMapper;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.LoginApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthLoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginApi loginApi;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String loginUser(String username, String password) {
        loginApi.getApiClient().setUsername(username);
        loginApi.getApiClient().setPassword(password);

        User user;

        try {
            log.info("Attempting to log user {}", username);
            AuthLoginResponseDto loginResponseDto = loginApi.loginUser();
            user = userMapper.map(loginResponseDto);

        } catch (HttpClientErrorException e) {
            log.error("Client error {} while attempting to login user {}", e.getStatusCode(), username);

            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new UserDoesNotExistException();
            } else if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new UserIsUnauthorizedException();
            } else if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                throw new UserBadPasswordException();
            } else if (e.getStatusCode().equals(HttpStatus.LOCKED)) {
                throw new UserIsLockedException();
            }
            throw new SafeboxMainException();

        } catch (HttpServerErrorException e) {
            log.error("Server error {} while attempting to login user {}", e.getStatusCode(), username);
            throw new ExternalServiceException();
        } catch (ResourceAccessException e) {
            log.error("Unknown error while attempting to login user", e);
            throw new ExternalServiceException();
        }

        log.info("User {} logged in successfully", username);

        return user.getToken();
    }
}
