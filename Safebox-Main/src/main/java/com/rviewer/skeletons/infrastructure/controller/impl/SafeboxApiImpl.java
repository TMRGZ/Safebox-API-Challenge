package com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.CreatedSafeboxDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxKeyDto;
import com.rviewer.skeletons.infrastructure.controller.SafeboxApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SafeboxApiImpl implements SafeboxApi {

    @Override
    public ResponseEntity<ItemListDto> getSafeboxItems(String id) {
        return SafeboxApi.super.getSafeboxItems(id);
    }

    @Override
    public ResponseEntity<SafeboxKeyDto> openSafebox(String id) {
        return SafeboxApi.super.openSafebox(id);
    }

    @Override
    public ResponseEntity<CreatedSafeboxDto> postSafebox(CreateSafeboxRequestDto createSafeboxRequestDto) {
        return SafeboxApi.super.postSafebox(createSafeboxRequestDto);
    }

    @Override
    public ResponseEntity<Void> putSafeboxItems(String id, ItemListDto itemListDto) {
        return SafeboxApi.super.putSafeboxItems(id, itemListDto);
    }
}
