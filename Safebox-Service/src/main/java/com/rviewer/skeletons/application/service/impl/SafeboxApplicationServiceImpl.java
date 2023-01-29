package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.SafeboxPost200Response;
import com.rviewer.skeletons.application.service.SafeboxApplicationService;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.service.SafeboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SafeboxApplicationServiceImpl implements SafeboxApplicationService {

    @Autowired
    private SafeboxService safeboxService;

    @Override
    @Transactional
    public ResponseEntity<SafeboxPost200Response> createSafebox(String owner) {
        safeboxService.createSafebox(owner);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addItemsToSafebox(Long safeboxId, List<String> itemDetailList) {
        List<Item> itemList = itemDetailList.stream()
                .map(Item::new)
                .collect(Collectors.toList());

        safeboxService.addItemsToSafebox(safeboxId, itemList);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> lockSafebox(Long id) {
        safeboxService.lockSafebox(id);
        return ResponseEntity.ok().build();
    }
}