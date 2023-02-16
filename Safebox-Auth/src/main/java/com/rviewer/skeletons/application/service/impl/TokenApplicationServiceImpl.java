package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.UserDto;
import com.rviewer.skeletons.application.service.TokenApplicationService;
import com.rviewer.skeletons.domain.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TokenApplicationServiceImpl implements TokenApplicationService {

    @Autowired
    private TokenService tokenService;

    @Override
    public ResponseEntity<UserDto> decodeToken() {
        String username = tokenService.getDecodedUsername();
        return ResponseEntity.ok(new UserDto().username(username));
    }
}
