package com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.CreatedSafeboxDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxKeyDto;
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
    public ResponseEntity<ItemListDto> getSafeboxItems(String id) {
        return safeboxApplicationService.getSafeboxItems(id);
    }

    @Override
    public ResponseEntity<SafeboxKeyDto> openSafebox(String id) {
        return safeboxApplicationService.openSafebox(id);
    }

    @Override
    public ResponseEntity<CreatedSafeboxDto> postSafebox(CreateSafeboxRequestDto createSafeboxRequestDto) {
        return safeboxApplicationService.createSafebox(createSafeboxRequestDto);
    }

    @Override
    public ResponseEntity<Void> putSafeboxItems(String id, ItemListDto itemListDto) {
        return safeboxApplicationService.addItemsToSafebox(id, itemListDto);
    }
}
