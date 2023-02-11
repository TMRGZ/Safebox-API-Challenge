package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.User;

import java.util.List;

public interface SafeboxService {

    String openSafebox(String id);

    String createSafebox(User user);

    List<Item> getSafeboxItems(String id);

    void addItemsToSafebox(String safeboxId, List<Item> itemList);

}
