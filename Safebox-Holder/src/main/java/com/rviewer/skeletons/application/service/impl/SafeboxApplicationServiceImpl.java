package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxDto;
import com.rviewer.skeletons.application.service.SafeboxApplicationService;
import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.exception.SafeboxServiceException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.service.SafeboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<SafeboxDto> createSafebox(String owner) {
        ResponseEntity<SafeboxDto> response;

        try {
            Safebox newSafebox = safeboxService.createSafebox(owner);
            SafeboxDto safeboxDto = new SafeboxDto().id(newSafebox.getId());
            response = new ResponseEntity<>(safeboxDto, HttpStatus.CREATED);

        } catch (SafeboxAlreadyExistsException e) {
            response = ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (SafeboxServiceException e) {
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addItemsToSafebox(String safeboxId, List<String> itemDetailList) {
        ResponseEntity<Void> response;

        try {
            List<Item> itemList = itemDetailList.stream()
                    .map(Item::new)
                    .collect(Collectors.toList());

            safeboxService.addItemsToSafebox(safeboxId, itemList);

            response = ResponseEntity.ok().build();

        } catch (SafeboxDoesNotExistException e) {
            response = ResponseEntity.notFound().build();
        } catch (SafeboxServiceException e) {
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }

    @Override
    public ResponseEntity<ItemListDto> getSafeboxItems(String safeboxId) {
        ResponseEntity<ItemListDto> response;

        try {
            List<Item> itemList = safeboxService.getSafeboxItems(safeboxId);
            List<String> itemDetails = itemList.stream().map(Item::getDetail).toList();
            response = ResponseEntity.ok().body(new ItemListDto().items(itemDetails));

        } catch (SafeboxDoesNotExistException e) {
            response = ResponseEntity.notFound().build();
        } catch (SafeboxServiceException e) {
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }
}
