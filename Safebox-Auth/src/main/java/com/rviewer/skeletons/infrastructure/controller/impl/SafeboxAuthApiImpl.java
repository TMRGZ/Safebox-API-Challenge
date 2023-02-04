package com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.application.model.RegisteredUserDto;
import com.rviewer.skeletons.application.model.UserDto;
import com.rviewer.skeletons.application.service.UserApplicationService;
import com.rviewer.skeletons.infrastructure.controller.SafeboxAuthApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SafeboxAuthApiImpl implements SafeboxAuthApi {

    @Autowired
    private UserApplicationService userApplicationService;


    @Override
    public ResponseEntity<LoginResponseDto> safeboxAuthIdLoginPost(String id) {
        return userApplicationService.loginUser(id);
    }

    @Override
    public ResponseEntity<RegisteredUserDto> safeboxAuthUserPost(UserDto userDto) {
        return userApplicationService.createUser(userDto);
    }
}
