package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.application.service.LoginApplicationService;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginApplicationServiceImpl implements LoginApplicationService {

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public ResponseEntity<LoginResponseDto> loginUser() {
        ResponseEntity<LoginResponseDto> response;

        try {
            String token = tokenService.generate();
            response = ResponseEntity.ok(new LoginResponseDto().token(token));

        } catch (UserDoesNotExistException e) {
            response = ResponseEntity.notFound().build();
        }

        return response;
    }
}
