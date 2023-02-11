package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;

import java.util.List;

public interface SafeboxService {

    String openSafebox(String id);

    String createSafebox(Safebox safebox);

    List<Item> getSafeboxItems(String id);

    void addItemsToSafebox(String safeboxId, List<Item> itemList);

}
