package com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.SafeboxAuthIdKeyGet200ResponseDto;
import com.rviewer.skeletons.application.model.SafeboxAuthPost200ResponseDto;
import com.rviewer.skeletons.application.model.SafeboxAuthPostRequestDto;
import com.rviewer.skeletons.application.service.UserApplicationService;
import com.rviewer.skeletons.infrastructure.controller.SafeboxAuthApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SafeboxAuthApiImpl implements SafeboxAuthApi {

    @Autowired
    private UserApplicationService userApplicationService;

    @Override
    public ResponseEntity<SafeboxAuthIdKeyGet200ResponseDto> safeboxAuthIdKeyGet(String id) {
        return SafeboxAuthApi.super.safeboxAuthIdKeyGet(id);
    }

    @Override
    public ResponseEntity<SafeboxAuthPost200ResponseDto> safeboxAuthPost(SafeboxAuthPostRequestDto safeboxAuthPostRequestDto) {
        return userApplicationService.createUser(safeboxAuthPostRequestDto);
    }
}
