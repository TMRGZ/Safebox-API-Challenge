package com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.SafeboxIdItemsGet200Response;
import com.rviewer.skeletons.application.model.SafeboxIdItemsGetRequest;
import com.rviewer.skeletons.application.model.SafeboxPost200Response;
import com.rviewer.skeletons.application.model.SafeboxPostRequest;
import com.rviewer.skeletons.application.service.SafeboxApplicationService;
import com.rviewer.skeletons.infrastructure.controller.SafeboxApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SafeboxApiImpl implements SafeboxApi {

    @Autowired
    private SafeboxApplicationService safeboxApplicationService;

    @Override
    public ResponseEntity<SafeboxIdItemsGet200Response> safeboxIdItemsGet(String id) {
        return SafeboxApi.super.safeboxIdItemsGet(id);
    }

    @Override
    public ResponseEntity<Void> safeboxIdItemsPut(String id, SafeboxIdItemsGetRequest safeboxIdItemsGetRequest) {
        return safeboxApplicationService.addItemsToSafebox(Long.parseLong(id), safeboxIdItemsGetRequest.getItems());
    }

    @Override
    public ResponseEntity<Void> safeboxIdLockPatch(String id) {
        return safeboxApplicationService.lockSafebox(Long.parseLong(id));
    }

    @Override
    public ResponseEntity<SafeboxPost200Response> safeboxPost(SafeboxPostRequest safeboxPostRequest) {
        return safeboxApplicationService.createSafebox(safeboxPostRequest.getOwner());
    }
}
