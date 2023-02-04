package com.rviewer.skeletons.application.service;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.application.model.RegisteredUserDto;
import com.rviewer.skeletons.application.model.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserApplicationService {

    ResponseEntity<RegisteredUserDto> createUser(UserDto request);

    ResponseEntity<LoginResponseDto> loginUser(String userId);

}
