package com.rviewer.skeletons.application.service;

import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SafeboxApplicationService {

    ResponseEntity<SafeboxDto> getSafebox(String owner);

    ResponseEntity<SafeboxDto> createSafebox(String owner);

    ResponseEntity<Void> addItemsToSafebox(String safeboxId, List<String> itemList);

    ResponseEntity<ItemListDto> getSafeboxItems(String safeboxId);

}
