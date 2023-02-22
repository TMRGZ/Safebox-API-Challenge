package com.rviewer.skeletons.domain.service;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;

import java.util.List;

public interface SafeboxHolderService {

    Safebox getSafebox(String id);

    List<Item> getSafeboxItems(String id);

    void putSafeboxItems(String id, List<Item> itemList);

}
