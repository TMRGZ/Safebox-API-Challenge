package com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxDto;
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
    public ResponseEntity<ItemListDto> safeboxIdItemsGet(String id) {
        return safeboxApplicationService.getSafeboxItems(id);
    }

    @Override
    public ResponseEntity<Void> safeboxIdItemsPut(String id, ItemListDto itemListDto) {
        return safeboxApplicationService.addItemsToSafebox(id, itemListDto.getItems());
    }

    @Override
    public ResponseEntity<SafeboxDto> safeboxPost(CreateSafeboxRequestDto request) {
        return safeboxApplicationService.createSafebox(request.getOwner());
    }
}
