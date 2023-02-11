package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.service.LoginService;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.LoginApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthLoginResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginApi loginApi;

    @Override
    public String loginUser(String username, String password) {
        loginApi.getApiClient().setUsername(username);
        loginApi.getApiClient().setPassword(password);

        AuthLoginResponseDto loginResponseDto = loginApi.loginUser();

        return loginResponseDto.getToken();
    }
}
