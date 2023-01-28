package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;

import java.util.List;

public interface SafeboxService {

    Safebox createSafebox(String owner);

    void addItemsToSafebox(Long safeboxId, List<Item> itemList);

    void lockSafebox(Long id);

}
