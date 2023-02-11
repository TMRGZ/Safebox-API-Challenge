package com.rviewer.skeletons.application.service;

import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.CreatedSafeboxDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxKeyDto;
import org.springframework.http.ResponseEntity;

public interface SafeboxApplicationService {

    ResponseEntity<CreatedSafeboxDto> createSafebox(CreateSafeboxRequestDto createSafeboxRequestDt);

    ResponseEntity<SafeboxKeyDto> openSafebox(String id);

    ResponseEntity<ItemListDto> getSafeboxItems(String id);

    ResponseEntity<Void> addItemsToSafebox(String id, ItemListDto itemListDto);

}
