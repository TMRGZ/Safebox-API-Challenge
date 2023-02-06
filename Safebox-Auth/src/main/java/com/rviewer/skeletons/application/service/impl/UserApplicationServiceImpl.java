package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.application.model.RegisteredUserDto;
import com.rviewer.skeletons.application.model.UserDto;
import com.rviewer.skeletons.application.service.UserApplicationService;
import com.rviewer.skeletons.domain.exception.UserAlreadyRegisteredException;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        ResponseEntity<RegisteredUserDto> response;

        try {
            SafeboxUser newSafeboxUser = userService.createUser(request.getUsername(), request.getPassword());
            response = new ResponseEntity<>(new RegisteredUserDto().id(newSafeboxUser.getId()), HttpStatus.CREATED);

        } catch (UserAlreadyRegisteredException e) {
            response = new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return response;
    }

    @Override
    @Transactional
    public ResponseEntity<LoginResponseDto> loginUser(String userId) {
        ResponseEntity<LoginResponseDto> response;

        try {
            String token = userService.generateUserToken(userId);
            response = ResponseEntity.ok(new LoginResponseDto().token(token));

        } catch (UserDoesNotExistException e) {
            response = ResponseEntity.notFound().build();
        }

        return response;
    }
}
