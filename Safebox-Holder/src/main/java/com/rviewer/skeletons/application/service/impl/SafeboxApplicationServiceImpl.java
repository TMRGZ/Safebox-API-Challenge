package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.mapper.InputItemMapper;
import com.rviewer.skeletons.application.mapper.InputSafeboxMapper;
import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxDto;
import com.rviewer.skeletons.application.service.SafeboxApplicationService;
import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.exception.SafeboxHolderException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.service.SafeboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class SafeboxApplicationServiceImpl implements SafeboxApplicationService {

    @Autowired
    private SafeboxService safeboxService;

    @Autowired
    private InputSafeboxMapper inputSafeboxMapper;

    @Autowired
    private InputItemMapper inputItemMapper;

    @Override
    public ResponseEntity<SafeboxDto> getSafebox(String owner) {
        ResponseEntity<SafeboxDto> response;

        try {
            log.info("Attempting to get safebox from {}", owner);

            Safebox safebox = safeboxService.getSafebox(owner);
            response = ResponseEntity.ok(inputSafeboxMapper.map(safebox));

            log.info("{}'s safebox retrieved with id {}", owner, safebox.getId());

        } catch (SafeboxDoesNotExistException e) {
            log.error("{}'s safebox does not exist", owner);
            response = ResponseEntity.notFound().build();
        } catch (SafeboxHolderException e) {
            log.error("An unknown error happened", e);
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }

    @Override
    @Transactional
    public ResponseEntity<SafeboxDto> createSafebox(CreateSafeboxRequestDto createSafeboxRequestDto) {
        ResponseEntity<SafeboxDto> response;

        try {
            log.info("Attempting to create a safebox for {}", createSafeboxRequestDto.getOwner());

            Safebox newSafebox = safeboxService.createSafebox(createSafeboxRequestDto.getOwner());
            response = new ResponseEntity<>(inputSafeboxMapper.map(newSafebox), HttpStatus.CREATED);

            log.info("{}'s safebox successfully created", createSafeboxRequestDto.getOwner());

        } catch (SafeboxAlreadyExistsException e) {
            log.error("{} already has a safebox, cancelling", createSafeboxRequestDto.getOwner());
            response = ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (SafeboxHolderException e) {
            log.error("An unknown error happened", e);
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addItemsToSafebox(String owner, ItemListDto itemListDto) {
        ResponseEntity<Void> response;

        try {
            log.info("Attempting to add {} items to {}'s safebox", itemListDto.getItems().size(), owner);

            List<Item> itemList = inputItemMapper.map(itemListDto);
            safeboxService.addItemsToSafebox(owner, itemList);
            response = ResponseEntity.status(HttpStatus.CREATED).build();

            log.info("{} items successfully added to {}'s safebox", itemListDto.getItems().size(), owner);

        } catch (SafeboxDoesNotExistException e) {
            log.error("{}'s safebox does not exist", owner);
            response = ResponseEntity.notFound().build();
        } catch (SafeboxHolderException e) {
            log.error("An unknown error happened", e);
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }

    @Override
    public ResponseEntity<ItemListDto> getSafeboxItems(String owner) {
        ResponseEntity<ItemListDto> response;

        try {
            log.info("Attempting to retrieve items from {}'s safebox", owner);

            List<Item> itemList = safeboxService.getSafeboxItems(owner);
            response = ResponseEntity.ok().body(inputItemMapper.map(itemList));

            log.info("Found {} items from {}'s safebox", itemList.size(), owner);

        } catch (SafeboxDoesNotExistException e) {
            log.error("{}'s safebox does not exist", owner);
            response = ResponseEntity.notFound().build();
        } catch (SafeboxHolderException e) {
            log.error("An unknown error happened", e);
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }
}
