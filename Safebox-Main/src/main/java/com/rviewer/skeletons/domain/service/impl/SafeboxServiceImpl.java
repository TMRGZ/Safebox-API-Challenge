package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.SafeboxService;
import com.rviewer.skeletons.domain.service.SafeboxHolderService;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SafeboxServiceImpl implements SafeboxService {

    private UserService userService;

    private SafeboxHolderService safeboxHolderService;

    private TokenService tokenService;


    @Override
    public String openSafebox(String id) {
        safeboxHolderService.getSafebox(id);
        return tokenService.retrieveCurrenUserToken();
    }

    @Override
    public String createSafebox(User user) {
        return userService.createUser(user.getUsername(), user.getPassword());
    }

    @Override
    public List<Item> getSafeboxItems(String id) {
        safeboxHolderService.getSafeboxItems(id);
        return null;
    }

    @Override
    public void addItemsToSafebox(String safeboxId, List<Item> itemList) {
        safeboxHolderService.putSafeboxItems(safeboxId, itemList);
    }
}
