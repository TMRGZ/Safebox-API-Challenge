package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.mapper.InputItemMapper;
import com.rviewer.skeletons.application.mapper.InputUserMapper;
import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.CreatedSafeboxDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxKeyDto;
import com.rviewer.skeletons.application.service.SafeboxApplicationService;
import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.SafeboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SafeboxApplicationServiceImpl implements SafeboxApplicationService {

    @Autowired
    private SafeboxService safeboxService;

    @Autowired
    private InputItemMapper itemMapper;

    @Autowired
    private InputUserMapper userMapper;

    @Override
    public ResponseEntity<CreatedSafeboxDto> createSafebox(CreateSafeboxRequestDto createSafeboxRequestDto) {
        ResponseEntity<CreatedSafeboxDto> response;

        try {
            User user = userMapper.map(createSafeboxRequestDto);
            String safeboxId = safeboxService.createSafebox(user);
            response = ResponseEntity.status(HttpStatus.CREATED).body(new CreatedSafeboxDto().id(safeboxId));

        } catch (SafeboxAlreadyExistsException e) {
            response = ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ExternalServiceException e) {
            response = ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } catch (SafeboxMainException e) {
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }

    @Override
    public ResponseEntity<SafeboxKeyDto> openSafebox(String id) {
        ResponseEntity<SafeboxKeyDto> response;

        try {
            String token = safeboxService.openSafebox(id);
            response = ResponseEntity.ok(new SafeboxKeyDto().token(token));

        } catch (SafeboxDoesNotExistException e) {
            response = ResponseEntity.notFound().build();
        } catch (ExternalServiceException e) {
            response = ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } catch (SafeboxMainException e) {
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }

    @Override
    public ResponseEntity<ItemListDto> getSafeboxItems(String id) {
        ResponseEntity<ItemListDto> response;

        try {
            List<Item> itemList = safeboxService.getSafeboxItems(id);
            response = ResponseEntity.ok(itemMapper.map(itemList));

        } catch (SafeboxDoesNotExistException e) {
            response = ResponseEntity.notFound().build();
        } catch (ExternalServiceException e) {
            response = ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } catch (SafeboxMainException e) {
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }

    @Override
    public ResponseEntity<Void> addItemsToSafebox(String id, ItemListDto itemListDto) {
        List<Item> itemList = itemMapper.map(itemListDto);
        ResponseEntity<Void> response;

        try {
            safeboxService.addItemsToSafebox(id, itemList);
            response = ResponseEntity.ok().build();

        } catch (SafeboxDoesNotExistException e) {
            response = ResponseEntity.notFound().build();
        } catch (ExternalServiceException e) {
            response = ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } catch (SafeboxMainException e) {
            response = ResponseEntity.internalServerError().build();
        }

        return response;
    }
}
