package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.service.SafeboxService;
import com.rviewer.skeletons.domain.service.SafeboxServiceService;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SafeboxServiceImpl implements SafeboxService {

    private UserService userService;

    private SafeboxServiceService safeboxServiceService;

    private TokenService tokenService;


    @Override
    public String openSafebox(String id) {
        safeboxServiceService.getSafebox(id);
        tokenService.retrieveCurrenUserToken();
        return null;
    }

    @Override
    public String createSafebox(Safebox safebox) {
        userService.createUser();
        return null;
    }

    @Override
    public List<Item> getSafeboxItems(String id) {
        safeboxServiceService.getSafeboxItems(id);
        return null;
    }

    @Override
    public void addItemsToSafebox(String safeboxId, List<Item> itemList) {
        safeboxServiceService.putSafeboxItems(safeboxId, itemList);
    }
}
