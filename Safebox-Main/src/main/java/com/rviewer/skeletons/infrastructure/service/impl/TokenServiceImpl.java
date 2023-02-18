package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.TokenApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenApi tokenApi;

    @Override
    public User decodeToken(String token) {
        tokenApi.getApiClient().setBearerToken(token);
        AuthUserDto userDto = tokenApi.decodeToken();

        User user = new User();
        user.setUsername(userDto.getUsername());

        return user;
    }

    @Override
    public String retrieveCurrenUserToken() {
        return SecurityContextHolder.getContext().getAuthentication().getDetails().toString();
    }
}
