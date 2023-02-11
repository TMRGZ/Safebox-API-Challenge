package com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.CreatedSafeboxDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxKeyDto;
import com.rviewer.skeletons.application.service.SafeboxApplicationService;
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

    @Override
    public ResponseEntity<CreatedSafeboxDto> createSafebox(CreateSafeboxRequestDto createSafeboxRequestDto) {
        User user = new User();
        user.setUsername(createSafeboxRequestDto.getName());
        user.setPassword(createSafeboxRequestDto.getPassword());

        String safeboxId = safeboxService.createSafebox(user);

        return new ResponseEntity<>(new CreatedSafeboxDto().id(safeboxId), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SafeboxKeyDto> openSafebox(String id) {
        String token = safeboxService.openSafebox(id);
        return ResponseEntity.ok(new SafeboxKeyDto().token(token));
    }

    @Override
    public ResponseEntity<ItemListDto> getSafeboxItems(String id) {
        List<Item> itemList = safeboxService.getSafeboxItems(id);
        List<String> itemDetailList = itemList.stream().map(Item::getDetail).toList();
        return ResponseEntity.ok(new ItemListDto().items(itemDetailList));
    }

    @Override
    public ResponseEntity<Void> addItemsToSafebox(String id, ItemListDto itemListDto) {
        List<Item> itemList = itemListDto.getItems().stream().map(Item::new).toList();
        safeboxService.addItemsToSafebox(id, itemList);
        return ResponseEntity.ok().build();
    }
}
