package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;

import java.util.List;

public interface SafeboxService {

    Safebox createSafebox(String key);

    void addItemsToSafebox(String safeboxId, List<Item> itemList);

    List<Item> getSafeboxItems(String safeboxId);

}
