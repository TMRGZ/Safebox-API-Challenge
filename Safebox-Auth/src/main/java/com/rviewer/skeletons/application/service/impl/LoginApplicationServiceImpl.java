package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.application.service.LoginApplicationService;
import com.rviewer.skeletons.domain.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginApplicationServiceImpl implements LoginApplicationService {

    @Autowired
    private TokenService tokenService;

    @Override
    public ResponseEntity<LoginResponseDto> loginUser() {
        String token = tokenService.generate();
        return ResponseEntity.ok(new LoginResponseDto().token(token));
    }
}
