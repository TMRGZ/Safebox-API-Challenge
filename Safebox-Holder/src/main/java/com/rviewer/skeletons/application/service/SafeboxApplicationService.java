package com.rviewer.skeletons.application.service;

import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxDto;
import org.springframework.http.ResponseEntity;

public interface SafeboxApplicationService {

    ResponseEntity<SafeboxDto> getSafebox(String owner);

    ResponseEntity<SafeboxDto> createSafebox(CreateSafeboxRequestDto createSafeboxRequestDto);

    ResponseEntity<Void> addItemsToSafebox(String safeboxId, ItemListDto itemListDto);

    ResponseEntity<ItemListDto> getSafeboxItems(String safeboxId);

}
