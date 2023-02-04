package com.rviewer.skeletons.application.service;

import com.rviewer.skeletons.application.model.SafeboxAuthIdKeyGet200ResponseDto;
import com.rviewer.skeletons.application.model.SafeboxAuthPost200ResponseDto;
import com.rviewer.skeletons.application.model.SafeboxAuthPostRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserApplicationService {

    ResponseEntity<SafeboxAuthPost200ResponseDto> createUser(SafeboxAuthPostRequestDto request);

    ResponseEntity<SafeboxAuthIdKeyGet200ResponseDto> loginUser();

}
