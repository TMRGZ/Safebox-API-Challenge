package com.rviewer.skeletons.application.service;

import com.rviewer.skeletons.application.model.UserDto;
import org.springframework.http.ResponseEntity;

public interface TokenApplicationService {

    ResponseEntity<UserDto> decodeToken();

}
