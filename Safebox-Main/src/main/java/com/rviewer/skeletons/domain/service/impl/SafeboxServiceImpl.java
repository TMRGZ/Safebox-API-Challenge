package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.SafeboxHolderService;
import com.rviewer.skeletons.domain.service.SafeboxService;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class SafeboxServiceImpl implements SafeboxService {

    private UserService userService;

    private SafeboxHolderService safeboxHolderService;

    private TokenService tokenService;


    @Override
    public String openSafebox(String id) {
        log.info("Retrieving Safebox with ID {}", id);
        safeboxHolderService.getSafebox(id);

        log.info("Retrieving current logged in user token");
        return tokenService.retrieveCurrenUserToken();
    }

    @Override
    public String createSafebox(User user) {
        log.info("Creating user {}", user.getUsername());
        return userService.createUser(user.getUsername(), user.getPassword());
    }

    @Override
    public List<Item> getSafeboxItems(String id) {
        log.info("Getting items from safebox {}", id);
        return safeboxHolderService.getSafeboxItems(id);
    }

    @Override
    public void addItemsToSafebox(String safeboxId, List<Item> itemList) {
        log.info("Adding to safebox {} {} items", safeboxId, itemList.size());
        safeboxHolderService.putSafeboxItems(safeboxId, itemList);
    }
}
