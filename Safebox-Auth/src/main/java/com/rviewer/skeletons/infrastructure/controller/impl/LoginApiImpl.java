package com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.application.service.LoginApplicationService;
import com.rviewer.skeletons.infrastructure.controller.LoginApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginApiImpl implements LoginApi {

    @Autowired
    private LoginApplicationService loginApplicationService;

    @Override
    public ResponseEntity<LoginResponseDto> loginUser() {
        return loginApplicationService.loginUser();
    }

}
