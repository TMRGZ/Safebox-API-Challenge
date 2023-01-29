package com.rviewer.skeletons.application.service;

import com.rviewer.skeletons.application.model.SafeboxIdItemsGet200Response;
import com.rviewer.skeletons.application.model.SafeboxPost200Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SafeboxApplicationService {

    ResponseEntity<SafeboxPost200Response> createSafebox(String owner);

    ResponseEntity<Void> addItemsToSafebox(String safeboxId, List<String> itemList);

    ResponseEntity<SafeboxIdItemsGet200Response> getSafeboxItems(String safeboxId);

}
