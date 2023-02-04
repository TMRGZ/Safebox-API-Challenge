package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.SafeboxAuthIdKeyGet200ResponseDto;
import com.rviewer.skeletons.application.model.SafeboxAuthPost200ResponseDto;
import com.rviewer.skeletons.application.model.SafeboxAuthPostRequestDto;
import com.rviewer.skeletons.application.service.UserApplicationService;
import com.rviewer.skeletons.domain.model.user.User;
import com.rviewer.skeletons.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public ResponseEntity<SafeboxAuthPost200ResponseDto> createUser(SafeboxAuthPostRequestDto request) {
        User newUser = userService.createUser(request.getName(), request.getPassword());

        return ResponseEntity.ok(new SafeboxAuthPost200ResponseDto().id(newUser.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<SafeboxAuthIdKeyGet200ResponseDto> loginUser() {
        String token = userService.loginUser("", "");

        return ResponseEntity.ok(new SafeboxAuthIdKeyGet200ResponseDto().token(token));
    }
}
