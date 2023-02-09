package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.application.model.safebox.auth.RegisteredUserDto;
import com.rviewer.skeletons.application.model.safebox.auth.UserDto;
import com.rviewer.skeletons.domain.service.UserService;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserApi userApi;

    @Override
    public String createUser(String username, String password) {
        RegisteredUserDto registeredUserDto = userApi.postUser(new UserDto().username(username).password(password));
        return registeredUserDto.getId();
    }
}
