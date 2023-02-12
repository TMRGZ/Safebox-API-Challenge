package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.*;
import com.rviewer.skeletons.domain.service.LoginService;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.LoginApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthLoginResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginApi loginApi;

    @Override
    public String loginUser(String username, String password) {
        loginApi.getApiClient().setUsername(username);
        loginApi.getApiClient().setPassword(password);

        AuthLoginResponseDto loginResponseDto;

        try {
            loginResponseDto = loginApi.loginUser();

        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()) {
                case NOT_FOUND -> throw new UserDoesNotExistException();
                case UNAUTHORIZED -> throw new UserIsUnauthorizedException();
                case LOCKED -> throw new UserIsLockedException();
                default -> throw new SafeboxMainException();
            }

        } catch (HttpServerErrorException e) {
            throw new ExternalServiceException();
        }

        return loginResponseDto.getToken();
    }
}
