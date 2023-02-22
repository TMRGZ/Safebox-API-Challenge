package com.rviewer.skeletons.application.service;

import com.rviewer.skeletons.application.model.CreateUserDto;
import com.rviewer.skeletons.application.model.RegisteredUserDto;
import org.springframework.http.ResponseEntity;

public interface UserApplicationService {

    ResponseEntity<RegisteredUserDto> createUser(CreateUserDto request);


}
