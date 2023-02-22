package com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.UserDto;
import com.rviewer.skeletons.application.service.TokenApplicationService;
import com.rviewer.skeletons.infrastructure.controller.TokenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenApiImpl implements TokenApi {

    @Autowired
    private TokenApplicationService tokenApplicationService;

    @Override
    public ResponseEntity<UserDto> decodeToken() {
        return tokenApplicationService.decodeToken();
    }
}
