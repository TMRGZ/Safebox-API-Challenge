package com.rviewer.skeletons.infrastructure.mapper;

import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthLoginResponseDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthRegisteredUserDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    public User map(AuthUserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public User map(AuthRegisteredUserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public User map(AuthLoginResponseDto loginDto) {
        return modelMapper.map(loginDto, User.class);
    }
}
