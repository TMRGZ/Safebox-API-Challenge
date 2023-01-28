package com.rviewer.skeletons.application.service;

import com.rviewer.skeletons.application.model.SafeboxPost200Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SafeboxApplicationService {

    ResponseEntity<SafeboxPost200Response> createSafebox(String owner);

    ResponseEntity<Void> addItemsToSafebox(Long safeboxId, List<String> itemList);

    ResponseEntity<Void> lockSafebox(Long id);

}
