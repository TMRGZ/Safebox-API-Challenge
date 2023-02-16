package com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.CreateUserDto;
import com.rviewer.skeletons.application.model.RegisteredUserDto;
import com.rviewer.skeletons.application.service.UserApplicationService;
import com.rviewer.skeletons.infrastructure.controller.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiImpl implements UserApi {

    @Autowired
    private UserApplicationService userApplicationService;

    @Override
    public ResponseEntity<RegisteredUserDto> postUser(CreateUserDto createUserDto) {
        return userApplicationService.createUser(createUserDto);
    }
}
