package com.rviewer.skeletons.application.service;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import org.springframework.http.ResponseEntity;

public interface LoginApplicationService {

    ResponseEntity<LoginResponseDto> loginUser();

}
