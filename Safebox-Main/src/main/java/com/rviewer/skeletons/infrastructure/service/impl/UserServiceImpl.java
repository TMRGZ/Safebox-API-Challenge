package com.rviewer.skeletons.infrastructure.service.impl;


import com.rviewer.skeletons.domain.service.UserService;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.UserApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthRegisteredUserDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserApi userApi;

    @Override
    public String createUser(String username, String password) {
        AuthRegisteredUserDto registeredUserDto = userApi.postUser(new AuthUserDto().username(username).password(password));
        return registeredUserDto.getId();
    }
}
