package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.application.model.RegisteredUserDto;
import com.rviewer.skeletons.application.model.UserDto;
import com.rviewer.skeletons.application.service.UserApplicationService;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
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
    public ResponseEntity<RegisteredUserDto> createUser(UserDto request) {
        SafeboxUser newSafeboxUser = userService.createUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new RegisteredUserDto().id(newSafeboxUser.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<LoginResponseDto> loginUser(String userId) {
        String token = userService.generateUserToken(userId);
        return ResponseEntity.ok(new LoginResponseDto().token(token));
    }
}
